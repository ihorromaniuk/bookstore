package core.basesyntax.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.mapper.BookMapper;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private static final String TITLE = "Shining";
    private static final String AUTHOR = "Stephen King";
    private static final BigDecimal PRICE = BigDecimal.valueOf(100);
    private static final long ID = 1L;
    private static final String ISBN = "12345678";

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void save_saveBook_ok() {
        Book book = new Book();
        book.setTitle(TITLE);
        book.setAuthor(AUTHOR);
        book.setIsbn(ISBN);
        book.setPrice(PRICE);
        book.setDeleted(false);

        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                null,
                null,
                List.of(ID));

        BookDto bookDto = new BookDto(
                ID,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                null,
                null,
                Set.of(ID));

        when(bookMapper.createDtoToModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenAnswer((Answer<Book>) invocation -> {
            book.setId(ID);
            return book;
        });
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.save(requestDto);

        assertEquals(bookDto, actual);
    }
}