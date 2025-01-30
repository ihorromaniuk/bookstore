package core.basesyntax.bookstore.controller;

import static core.basesyntax.bookstore.util.TestUtil.AUTHOR;
import static core.basesyntax.bookstore.util.TestUtil.CONTENT_KEY;
import static core.basesyntax.bookstore.util.TestUtil.COVER_IMAGE;
import static core.basesyntax.bookstore.util.TestUtil.DESCRIPTION;
import static core.basesyntax.bookstore.util.TestUtil.ID;
import static core.basesyntax.bookstore.util.TestUtil.ID_AFTER_LAST_INSERTED_BOOK;
import static core.basesyntax.bookstore.util.TestUtil.ISBN;
import static core.basesyntax.bookstore.util.TestUtil.PRICE;
import static core.basesyntax.bookstore.util.TestUtil.TITLE;
import static core.basesyntax.bookstore.util.TestUtil.getDbBooksWithoutCategory;
import static core.basesyntax.bookstore.util.TestUtil.getDbHarryPotterBookDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import core.basesyntax.bookstore.dto.exception.ExceptionDto;
import core.basesyntax.bookstore.dto.exception.ValidationExceptionDto;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

@Sql(scripts = {
        "classpath:database/book/remove_all_books.sql",
        "classpath:database/category/remove_all_categories.sql",
        "classpath:database/category/insert_2_categories.sql",
        "classpath:database/book/insert_3_books.sql"
},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/book/remove_all_books.sql",
        "classpath:database/category/remove_all_categories.sql"
},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class BookControllerTest extends BaseControllerTest {
    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void findAll_getAllBooksFromDb_200() {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, Object> responseBody = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        List<BookWithoutCategoryDto> actual = objectMapper.readValue(
                objectMapper.writeValueAsString(responseBody.get(CONTENT_KEY)),
                new TypeReference<>() {
                });

        //then
        List<BookWithoutCategoryDto> expected = getDbBooksWithoutCategory();
        assertIterableEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getBookById_getBookFromDb_200() {
        MvcResult mvcResult = mockMvc.perform(get("/books/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        BookDto expected = getDbHarryPotterBookDto();
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getBookById_bookNotFound_404() {
        MvcResult mvcResult = mockMvc.perform(get("/books/"
                        + ID_AFTER_LAST_INSERTED_BOOK)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ExceptionDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(HttpStatus.NOT_FOUND,
                "Can't find book by id. Id: " + ID_AFTER_LAST_INSERTED_BOOK);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getBooksBySpec_getBookBySpec_200() {
        //given
        BookParamsDto bookParamsDto = new BookParamsDto("and",
                new String[]{"J.K. Rowling", "Q. Blake"});
        String jsonRequest = objectMapper.writeValueAsString(bookParamsDto);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/books/search")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        Map<String, Object> responseBody = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        List<BookWithoutCategoryDto> actual = objectMapper.readValue(
                objectMapper.writeValueAsString(responseBody.get(CONTENT_KEY)),
                new TypeReference<>() {
                });
        List<BookWithoutCategoryDto> expected = getDbBooksWithoutCategory().stream()
                .filter(dto -> Objects.equals(dto.id(), 2L)
                        || Objects.equals(dto.id(), 3L))
                .toList();
        assertIterableEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {"classpath:database/book/remove_shining_book.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @Test
    void createBook_createBook_201() {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );

        //when
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //then
        BookDto actualBody = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        BookDto expectedBody = new BookDto(
                0L,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                Set.of(ID));
        assertThat(actualBody)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBody);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void createBook_validationFailed_400() {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                TITLE,
                null,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        ValidationExceptionDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ValidationExceptionDto.class
        );
        ValidationExceptionDto expected = new ValidationExceptionDto(
                HttpStatus.BAD_REQUEST, Set.of("author must not be blank")
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/book/insert_mr_mercedes_book.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_mr_mercedes_book.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @Test
    void updateBook_updateBook_200() {
        //given
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );

        //when
        MvcResult mvcResult = mockMvc.perform(put("/books/"
                        + ID_AFTER_LAST_INSERTED_BOOK)
                        .content(objectMapper.writeValueAsBytes(updateBookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        BookDto expected = new BookDto(
                4L,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                Set.of(ID)
        );
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void updateBook_bookNotFound_404() {
        //given
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );

        //when
        MvcResult mvcResult = mockMvc.perform(put("/books/"
                        + ID_AFTER_LAST_INSERTED_BOOK)
                        .content(objectMapper.writeValueAsBytes(updateBookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        ExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                        ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(HttpStatus.NOT_FOUND,
                "Can't find book by id to update. Id: "
                        + ID_AFTER_LAST_INSERTED_BOOK);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void updateBook_validationFailed_400() {
        //given
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                null,
                AUTHOR,
                ISBN,
                null,
                DESCRIPTION,
                COVER_IMAGE,
                List.of()
        );

        //when
        MvcResult mvcResult = mockMvc.perform(put("/books/"
                        + ID_AFTER_LAST_INSERTED_BOOK)
                        .content(objectMapper.writeValueAsBytes(updateBookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        ValidationExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                        ValidationExceptionDto.class);
        ValidationExceptionDto expected = new ValidationExceptionDto(HttpStatus.BAD_REQUEST,
                Set.of(
                        "title must not be blank",
                        "price must not be null",
                        "categoryIds must not be empty"
                ));
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/book/insert_mr_mercedes_book.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_mr_mercedes_book.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @Test
    void deleteBook_deleteBookOk_204() {
        mockMvc.perform(delete("/books/" + ID_AFTER_LAST_INSERTED_BOOK))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void deleteBook_bookNotFound_404() {
        //when
        MvcResult mvcResult = mockMvc.perform(delete("/books/"
                        + ID_AFTER_LAST_INSERTED_BOOK))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        ExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                        ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(HttpStatus.NOT_FOUND,
                "Can't find book by id to delete. Id: " + ID_AFTER_LAST_INSERTED_BOOK);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }
}
