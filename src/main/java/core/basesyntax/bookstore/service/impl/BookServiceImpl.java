package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import core.basesyntax.bookstore.exception.EntityNotFoundException;
import core.basesyntax.bookstore.mapper.BookMapper;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.repository.SpecificationBuilder;
import core.basesyntax.bookstore.repository.book.BookRepository;
import core.basesyntax.bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationBuilder<Book> bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(
                bookRepository.save(bookMapper.createDtoToModel(requestDto))
        );
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id. Id: " + id)));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto requestDto) {
        if (bookRepository.existsById(id)) {
            Book book = bookMapper.updateDtoToModel(requestDto);
            book.setId(id);
            return bookMapper.toDto(bookRepository.save(book));
        }

        throw new EntityNotFoundException("Can't find book by id to update. Id: " + id);
    }

    @Override
    public List<BookDto> findAll(BookParamsDto params) {
        Specification<Book> spec = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(spec).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
