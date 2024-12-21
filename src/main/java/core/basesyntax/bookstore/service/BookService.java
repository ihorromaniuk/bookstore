package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getById(Long id);

    void delete(Long id);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    Page<BookWithoutCategoryDto> findAll(BookParamsDto params, Pageable pageable);

    Page<BookWithoutCategoryDto> findAll(Pageable pageable);

    Page<BookWithoutCategoryDto> findAllByCategoryId(Long categoryId, Pageable pageable);

    boolean existsById(Long id);
}
