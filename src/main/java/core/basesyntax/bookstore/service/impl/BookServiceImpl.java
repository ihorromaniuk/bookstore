package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import core.basesyntax.bookstore.exception.EntityNotFoundException;
import core.basesyntax.bookstore.mapper.BookMapper;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.model.Category;
import core.basesyntax.bookstore.repository.SpecificationBuilder;
import core.basesyntax.bookstore.repository.book.BookRepository;
import core.basesyntax.bookstore.service.BookService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Can't find book by id to delete. Id: " + id);
        }
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id to update. Id: " + id));
        bookMapper.updateBookFromDto(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookWithoutCategoryDto> findAll(BookParamsDto params, Pageable pageable) {
        Specification<Book> spec = bookSpecificationBuilder.build(params);
        Page<Book> page = bookRepository.findAll(spec, pageable);
        List<BookWithoutCategoryDto> bookWithoutCategoryDtoList = page
                .stream()
                .map(bookMapper::toDtoWithoutCategory)
                .toList();
        return new PageImpl<>(bookWithoutCategoryDtoList, pageable, page.getTotalElements());
    }

    @Override
    public Page<BookWithoutCategoryDto> findAll(Pageable pageable) {
        Page<Book> page = bookRepository.findAll(pageable);
        List<BookWithoutCategoryDto> bookWithoutCategoryDtoList = page.stream()
                .map(bookMapper::toDtoWithoutCategory)
                .toList();
        return new PageImpl<>(bookWithoutCategoryDtoList, pageable, page.getTotalElements());
    }

    @Override
    public Page<BookWithoutCategoryDto> findAllByCategoryId(Long categoryId, Pageable pageable) {
        Page<Book> page = bookRepository
                .findAllByCategoriesContains(Set.of(new Category(categoryId)), pageable);
        List<BookWithoutCategoryDto> bookDtoList = page.stream()
                .map(bookMapper::toDtoWithoutCategory)
                .toList();
        return new PageImpl<>(bookDtoList, pageable, page.getTotalElements());
    }

    @Override
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }
}
