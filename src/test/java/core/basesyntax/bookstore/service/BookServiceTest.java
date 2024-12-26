package core.basesyntax.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import core.basesyntax.bookstore.exception.EntityNotFoundException;
import core.basesyntax.bookstore.mapper.BookMapper;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.repository.SpecificationBuilder;
import core.basesyntax.bookstore.repository.book.BookRepository;
import core.basesyntax.bookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private static final String TITLE = "Shining";
    private static final String AUTHOR = "Stephen King";
    private static final BigDecimal PRICE = BigDecimal.valueOf(100);
    private static final long ID = 1L;
    private static final String ISBN = "12345678";
    private static final Book BOOK = new Book(ID);
    private static final BookDto BOOK_DTO = new BookDto(
            ID,
            TITLE,
            AUTHOR,
            ISBN,
            PRICE,
            null,
            null,
            Set.of(ID));

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private SpecificationBuilder<Book> bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    static void beforeAll() {
        BOOK.setTitle(TITLE);
        BOOK.setAuthor(AUTHOR);
        BOOK.setIsbn(ISBN);
        BOOK.setPrice(PRICE);
        BOOK.setDeleted(false);
    }

    @Test
    void save_saveBook_ok() {
        Book bookWithoutId = new Book();
        bookWithoutId.setTitle(TITLE);
        bookWithoutId.setAuthor(AUTHOR);
        bookWithoutId.setIsbn(ISBN);
        bookWithoutId.setPrice(PRICE);
        bookWithoutId.setDeleted(false);

        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                null,
                null,
                List.of(ID)
        );
        when(bookMapper.createDtoToModel(requestDto)).thenReturn(bookWithoutId);
        when(bookRepository.save(bookWithoutId)).thenReturn(BOOK);
        when(bookMapper.toDto(BOOK)).thenReturn(BOOK_DTO);

        BookDto actual = bookService.save(requestDto);

        assertEquals(BOOK_DTO, actual);
    }

    @Test
    void getById_getBookById_ok() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        when(bookMapper.toDto(BOOK)).thenReturn(BOOK_DTO);

        BookDto actual = bookService.getById(ID);
        assertEquals(BOOK_DTO, actual);
    }

    @Test
    void getById_getOptionalEmptyFromRepository_notOk() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(ID));
        String actualMessage = ex.getMessage();
        String expectedMessage = "Can't find book by id. Id: 1";

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void delete_deleteBook_ok() {
        when(bookRepository.existsById(ID)).thenReturn(true);

        bookService.delete(ID);

        verify(bookRepository, times(1)).deleteById(ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void delete_bookNotFound_notOk() {
        when(bookRepository.existsById(ID)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookService.delete(ID));
        String actualMessage = ex.getMessage();
        String expectedMessage = "Can't find book by id to delete. Id: 1";

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void update_updateBook_ok() {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                null,
                null,
                List.of(ID)
        );

        when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        when(bookRepository.save(BOOK)).thenReturn(BOOK);
        when(bookMapper.toDto(BOOK)).thenReturn(BOOK_DTO);

        BookDto actual = bookService.update(ID, requestDto);

        assertEquals(BOOK_DTO, actual);
        verify(bookMapper).updateBookFromDto(requestDto, BOOK);
    }

    @Test
    void update_bookNotFound_notOk() {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                null,
                null,
                List.of(ID)
        );

        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(ID, requestDto));
        String actualMessage = ex.getMessage();
        String expectedMessage = "Can't find book by id to update. Id: 1";

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void findAll_findAllWithSpecAndPagination_ok() {
        String[] authors = new String[] {AUTHOR};
        BookParamsDto bookParamsDto = new BookParamsDto(TITLE, authors);
        Pageable pageable = Pageable.unpaged();
        Page<Book> page = new PageImpl<>(List.of(BOOK), pageable, 1);
        BookWithoutCategoryDto bookDto = new BookWithoutCategoryDto(
                ID,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                null,
                null
        );

        when(bookRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(page);
        when(bookMapper.toDtoWithoutCategory(BOOK)).thenReturn(bookDto);

        Page<BookWithoutCategoryDto> expected =
                new PageImpl<>(List.of(bookDto), pageable, 1);

        Page<BookWithoutCategoryDto> actual =
                bookService.findAll(bookParamsDto, Pageable.unpaged());

        assertEquals(expected, actual);
        verify(bookSpecificationBuilder).build(bookParamsDto);
    }

    @Test
    void findAll_findAllWithPagination_ok() {
        Pageable pageable = Pageable.unpaged();
        Page<Book> page = new PageImpl<>(List.of(BOOK), pageable, 1);
        BookWithoutCategoryDto bookDto = new BookWithoutCategoryDto(
                ID,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                null,
                null
        );

        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(bookMapper.toDtoWithoutCategory(BOOK)).thenReturn(bookDto);

        Page<BookWithoutCategoryDto> expected =
                new PageImpl<>(List.of(bookDto), pageable, 1);

        Page<BookWithoutCategoryDto> actual =
                bookService.findAll(Pageable.unpaged());

        assertEquals(expected, actual);
    }

    @Test
    void findAllByCategoryId_findBookByCategoryId_ok() {
        Pageable pageable = Pageable.unpaged();
        Page<Book> page = new PageImpl<>(List.of(BOOK), pageable, 1);
        BookWithoutCategoryDto bookDto = new BookWithoutCategoryDto(
                ID,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                null,
                null
        );

        when(bookRepository.findAllByCategoriesContains(any(Set.class), eq(pageable)))
                .thenReturn(page);
        when(bookMapper.toDtoWithoutCategory(BOOK)).thenReturn(bookDto);

        Page<BookWithoutCategoryDto> expected =
                new PageImpl<>(List.of(bookDto), pageable, 1);

        Page<BookWithoutCategoryDto> actual =
                bookService.findAllByCategoryId(ID, pageable);

        assertEquals(expected, actual);
    }

    @Test
    void existsById_bookExistsById_ok() {
        when(bookRepository.existsById(ID)).thenReturn(true);

        assertTrue(bookService.existsById(ID));
    }
}
