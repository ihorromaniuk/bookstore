package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.mapper.BookMapper;
import core.basesyntax.bookstore.repository.BookRepository;
import core.basesyntax.bookstore.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.entityToDto(
                bookRepository.save(bookMapper.createRequestDtoToEntity(requestDto))
        );
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::entityToDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.entityToDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id. Id: " + id)));
    }
}

