package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getById(Long id);

    void delete(Long id);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    List<BookDto> findAll(BookParamsDto params, Pageable pageable);

    List<BookDto> findAll(Pageable pageable);
}
