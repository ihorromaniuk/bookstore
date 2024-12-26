package core.basesyntax.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import core.basesyntax.bookstore.dto.exception.ExceptionDto;
import core.basesyntax.bookstore.dto.exception.ValidationExceptionDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static final String TITLE = "Shining";
    private static final String AUTHOR = "Stephen King";
    private static final BigDecimal PRICE = BigDecimal.valueOf(100);
    private static final long ID = 1L;
    private static final String ISBN = "12345678";
    private static final String DESCRIPTION = "spooky book";
    private static final String COVER_IMAGE = "www.cool-images.com/shining";
    private static final String CONTENT_KEY = "content";

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        afterAll(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/insert_2_categories.sql"));
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/insert_3_books.sql"));
        }
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void findAll_getAllBooksFromDb_200() {
        MvcResult mvcResult = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
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
        BookWithoutCategoryDto charlie = new BookWithoutCategoryDto(
                3L,
                "Charlie and the Chocolate Factory",
                "Q. Blake",
                "9780241558324",
                BigDecimal.valueOf(400),
                "Book about Willy Wonka",
                "image url"
        );
        List<BookWithoutCategoryDto> expected =
                List.of(hobbit, harryPotter, charlie);

        assertEquals(expected, actual);
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

        BookDto expected = new BookDto(
                2L,
                "Harry Potter and the Philosopher's stone",
                "J.K. Rowling",
                "9781408855652",
                BigDecimal.valueOf(475),
                "Book about wizard",
                "image url",
                Set.of(2L)
        );

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getBookById_bookNotFound_404() {
        MvcResult mvcResult = mockMvc.perform(get("/books/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ExceptionDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(HttpStatus.NOT_FOUND,
                "Can't find book by id. Id: 4");

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user", roles = "USER")
    @SneakyThrows
    @Test
    void getBooksBySpec_getBookBySpec_200() {
        BookParamsDto bookParamsDto = new BookParamsDto("and",
                new String[]{"J.K. Rowling", "Q. Blake"});
        String jsonRequest = objectMapper.writeValueAsString(bookParamsDto);

        MvcResult mvcResult = mockMvc.perform(post("/books/search")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> responseBody = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        List<BookWithoutCategoryDto> actual = objectMapper.readValue(
                objectMapper.writeValueAsString(responseBody.get(CONTENT_KEY)),
                new TypeReference<>() {});

        BookWithoutCategoryDto harryPotter = new BookWithoutCategoryDto(
                2L,
                "Harry Potter and the Philosopher's stone",
                "J.K. Rowling",
                "9781408855652",
                BigDecimal.valueOf(475),
                "Book about wizard",
                "image url"
        );
        BookWithoutCategoryDto charlie = new BookWithoutCategoryDto(
                3L,
                "Charlie and the Chocolate Factory",
                "Q. Blake",
                "9780241558324",
                BigDecimal.valueOf(400),
                "Book about Willy Wonka",
                "image url"
        );
        List<BookWithoutCategoryDto> expected =
                List.of(harryPotter, charlie);

        assertEquals(expected, actual);
    }


    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {"classpath:database/book/remove_shining_book.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @Test
    void createBook_createBook_201() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );

        BookDto expectedBody = new BookDto(
                ID,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                Set.of(ID));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actualBody = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertThat(actualBody)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBody);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void createBook_validationFailed_400() {
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

        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
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
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );

        MvcResult mvcResult = mockMvc.perform(put("/books/4")
                        .content(objectMapper.writeValueAsBytes(updateBookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
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
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );
        MvcResult mvcResult = mockMvc.perform(put("/books/4")
                        .content(objectMapper.writeValueAsBytes(updateBookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(HttpStatus.NOT_FOUND,
                "Can't find book by id to update. Id: 4");

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void updateBook_validationFailed_400() {
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                null,
                AUTHOR,
                ISBN,
                null,
                DESCRIPTION,
                COVER_IMAGE,
                List.of()
        );
        MvcResult mvcResult = mockMvc.perform(put("/books/4")
                        .content(objectMapper.writeValueAsBytes(updateBookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
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
        mockMvc.perform(delete("/books/4"))
                .andExpect(status().isNoContent());
        System.out.println();
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @SneakyThrows
    @Test
    void deleteBook_bookNotFound_404() {
        MvcResult mvcResult = mockMvc.perform(delete("/books/4"))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                        ExceptionDto.class);
        ExceptionDto expected = new ExceptionDto(HttpStatus.NOT_FOUND,
                "Can't find book by id to delete. Id: 4");

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expected);
    }

    @SneakyThrows
    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove_all_books.sql"));
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/remove_all_categories.sql"));
        }
    }
}
