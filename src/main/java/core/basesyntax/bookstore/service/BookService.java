package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getById(Long id);

    void delete(Long id);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    List<BookDto> findAll(BookParamsDto params);

    List<BookDto> findAll();
}
