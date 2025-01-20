package core.basesyntax.bookstore.service;

import static core.basesyntax.bookstore.util.TestUtil.AUTHOR;
import static core.basesyntax.bookstore.util.TestUtil.COVER_IMAGE;
import static core.basesyntax.bookstore.util.TestUtil.DESCRIPTION;
import static core.basesyntax.bookstore.util.TestUtil.ID;
import static core.basesyntax.bookstore.util.TestUtil.ISBN;
import static core.basesyntax.bookstore.util.TestUtil.PRICE;
import static core.basesyntax.bookstore.util.TestUtil.TITLE;
import static core.basesyntax.bookstore.util.TestUtil.getShiningBook;
import static core.basesyntax.bookstore.util.TestUtil.getShiningBookDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import core.basesyntax.bookstore.repository.category.CategoryRepository;
import core.basesyntax.bookstore.service.impl.BookServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private SpecificationBuilder<Book> bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

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
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );
        when(bookMapper.createDtoToModel(requestDto)).thenReturn(bookWithoutId);
        when(bookRepository.save(bookWithoutId)).thenReturn(getShiningBook());
        when(bookMapper.toDto(getShiningBook())).thenReturn(getShiningBookDto());

        BookDto actual = bookService.save(requestDto);

        assertEquals(getShiningBookDto(), actual);
    }

    @Test
    void getById_getBookById_ok() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(getShiningBook()));
        when(bookMapper.toDto(getShiningBook())).thenReturn(getShiningBookDto());

        BookDto actual = bookService.getById(ID);

        assertEquals(getShiningBookDto(), actual);
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
                DESCRIPTION,
                COVER_IMAGE,
                List.of(ID)
        );
        when(bookRepository.findById(ID)).thenReturn(Optional.of(getShiningBook()));
        when(bookRepository.save(getShiningBook())).thenReturn(getShiningBook());
        when(bookMapper.toDto(getShiningBook())).thenReturn(getShiningBookDto());

        BookDto actual = bookService.update(ID, requestDto);

        assertEquals(getShiningBookDto(), actual);
        verify(bookMapper).updateBookFromDto(requestDto, getShiningBook());
    }

    @Test
    void update_bookNotFound_notOk() {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto(
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE,
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
        Page<Book> page = new PageImpl<>(List.of(getShiningBook()), pageable, 1);
        BookWithoutCategoryDto bookDto = new BookWithoutCategoryDto(
                ID,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE
        );
        when(bookRepository.findAll(nullable(Specification.class), eq(pageable))).thenReturn(page);
        when(bookMapper.toDtoWithoutCategory(getShiningBook())).thenReturn(bookDto);

        Page<BookWithoutCategoryDto> actual =
                bookService.findAll(bookParamsDto, Pageable.unpaged());

        Page<BookWithoutCategoryDto> expected =
                new PageImpl<>(List.of(bookDto), pageable, 1);
        assertEquals(expected, actual);
        verify(bookSpecificationBuilder).build(bookParamsDto);
    }

    @Test
    void findAll_findAllWithPagination_ok() {
        Pageable pageable = Pageable.unpaged();
        Page<Book> page = new PageImpl<>(List.of(getShiningBook()), pageable, 1);
        BookWithoutCategoryDto bookDto = new BookWithoutCategoryDto(
                ID,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE
        );
        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(bookMapper.toDtoWithoutCategory(getShiningBook())).thenReturn(bookDto);

        Page<BookWithoutCategoryDto> actual =
                bookService.findAll(Pageable.unpaged());

        Page<BookWithoutCategoryDto> expected =
                new PageImpl<>(List.of(bookDto), pageable, 1);
        assertEquals(expected, actual);
    }

    @Test
    void findAllByCategoryId_findBookByCategoryId_ok() {
        Pageable pageable = Pageable.unpaged();
        Page<Book> page = new PageImpl<>(List.of(getShiningBook()), pageable, 1);
        BookWithoutCategoryDto bookDto = new BookWithoutCategoryDto(
                ID,
                TITLE,
                AUTHOR,
                ISBN,
                PRICE,
                DESCRIPTION,
                COVER_IMAGE
        );
        when(categoryRepository.existsById(ID)).thenReturn(true);
        when(bookRepository.findAllByCategoriesContains(any(Set.class), eq(pageable)))
                .thenReturn(page);
        when(bookMapper.toDtoWithoutCategory(getShiningBook())).thenReturn(bookDto);

        Page<BookWithoutCategoryDto> actual =
                bookService.findAllByCategoryId(ID, pageable);

        Page<BookWithoutCategoryDto> expected =
                new PageImpl<>(List.of(bookDto), pageable, 1);
        assertEquals(expected, actual);
    }
}
