package core.basesyntax.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.category.CategoryDto;
import core.basesyntax.bookstore.dto.category.CreateCategoryRequestDto;
import core.basesyntax.bookstore.dto.category.UpdateCategoryRequestDto;
import core.basesyntax.bookstore.dto.exception.ExceptionDto;
import core.basesyntax.bookstore.dto.exception.ValidationExceptionDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

@Sql(scripts = {
        "classpath:database/category/remove_all_categories.sql",
        "classpath:database/category/insert_2_categories.sql"
},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/category/remove_all_categories.sql"
},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class CategoryControllerTest extends BaseControllerTest {
    private static final Long ID_AFTER_LAST_INSERTED_CATEGORY = 3L;
    private static final String NAME = "action";
    private static final String DESCRIPTION = "action genre";

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getAllCategories_getCategories_200() {
        MvcResult mvcResult = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, Object> responseBody = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        List<CategoryDto> actual = objectMapper.readValue(
                objectMapper.writeValueAsString(responseBody.get(CONTENT_KEY)),
                new TypeReference<>() {});

        CategoryDto adventureFromDb = new CategoryDto(
                1L,
                "adventure",
                "adventure genre"
        );
        CategoryDto fantasyFromDb = new CategoryDto(
                2L,
                "fantasy",
                "magic genre"
        );
        List<CategoryDto> expected = List.of(adventureFromDb, fantasyFromDb);

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getCategoryById_getCategory_200() {
        MvcResult mvcResult = mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        CategoryDto expected = new CategoryDto(
                1L,
                "adventure",
                "adventure genre"
        );

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getCategoryById_categoryNotFound_404() {
        MvcResult mvcResult = mockMvc.perform(get("/categories/"
                        + ID_AFTER_LAST_INSERTED_CATEGORY))
                .andExpect(status().isNotFound())
                .andReturn();
        ExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(HttpStatus.NOT_FOUND,
                "Can't find category by id. Id: " + ID_AFTER_LAST_INSERTED_CATEGORY);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/category/remove_action_category.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @Test
    void createCategory_createCategory_201() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto(
                NAME,
                DESCRIPTION
        );
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(createCategoryRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        CategoryDto expected = new CategoryDto(
                0L,
                NAME,
                DESCRIPTION
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void createCategory_validationFailed_400() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto(
                "",
                DESCRIPTION
        );
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(createCategoryRequestDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        ValidationExceptionDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ValidationExceptionDto.class
        );
        ValidationExceptionDto expected = new ValidationExceptionDto(
                HttpStatus.BAD_REQUEST,
                Set.of("name must not be blank")
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/category/insert_detective_category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/category/remove_action_category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @Test
    void updateCategory_updateCategory_200() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                NAME,
                DESCRIPTION
        );

        MvcResult mvcResult = mockMvc.perform(put("/categories/"
                        + ID_AFTER_LAST_INSERTED_CATEGORY)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        CategoryDto expected = new CategoryDto(ID_AFTER_LAST_INSERTED_CATEGORY,
                NAME,
                DESCRIPTION);

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void updateCategory_categoryNotFound_404() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                NAME,
                DESCRIPTION
        );

        MvcResult mvcResult = mockMvc.perform(put("/categories/"
                        + ID_AFTER_LAST_INSERTED_CATEGORY)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(
                HttpStatus.NOT_FOUND,
                "Can't find category by id. Id: " + ID_AFTER_LAST_INSERTED_CATEGORY
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void updateCategory_validationFailed_400() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                "",
                DESCRIPTION
        );

        MvcResult mvcResult = mockMvc.perform(put("/categories/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        ValidationExceptionDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ValidationExceptionDto.class
        );
        ValidationExceptionDto expected = new ValidationExceptionDto(
                HttpStatus.BAD_REQUEST,
                Set.of("name must not be blank")
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @Sql(scripts = {
            "classpath:database/category/insert_detective_category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/category/remove_detective_category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void deleteCategory_deleteCategory_204() {
        mockMvc.perform(delete("/categories/" + ID_AFTER_LAST_INSERTED_CATEGORY))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void deleteCategory_categoryNotFound_404() {
        MvcResult mvcResult = mockMvc.perform(delete("/categories/"
                        + ID_AFTER_LAST_INSERTED_CATEGORY))
                .andExpect(status().isNotFound())
                .andReturn();
        ExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(
                HttpStatus.NOT_FOUND,
                "Can't find category by id. Id: " + ID_AFTER_LAST_INSERTED_CATEGORY
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @Sql(scripts = {
            "classpath:database/book/insert_3_books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_all_books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getBooksByCategoryId_getBooksByCategory_200() {
        MvcResult mvcResult = mockMvc.perform(get("/categories/2/books"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> responseBody = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        List<BookWithoutCategoryDto> actual = objectMapper.readValue(
                objectMapper.writeValueAsString(responseBody.get(CONTENT_KEY)),
                new TypeReference<>() {});

        BookWithoutCategoryDto hobbit = new BookWithoutCategoryDto(
                1L,
                "Hobbit",
                "J.R.R. Tolkien",
                "9780261102217",
                BigDecimal.valueOf(500),
                "Book about adventures of hobbit",
                "image url"
        );
        BookWithoutCategoryDto harryPotter = new BookWithoutCategoryDto(
                2L,
                "Harry Potter and the Philosopher's stone",
                "J.K. Rowling",
                "9781408855652",
                BigDecimal.valueOf(475),
                "Book about wizard",
                "image url"
        );
        List<BookWithoutCategoryDto> expected = List.of(hobbit, harryPotter);

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getBooksByCategoryId_categoryNotFound_404() {
        MvcResult mvcResult = mockMvc.perform(get("/categories/" 
                        + ID_AFTER_LAST_INSERTED_CATEGORY 
                        + "/books"))
                .andExpect(status().isNotFound())
                .andReturn();
        ExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(
                HttpStatus.NOT_FOUND,
                "Can't find category by id. Id: " + ID_AFTER_LAST_INSERTED_CATEGORY
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }
}
