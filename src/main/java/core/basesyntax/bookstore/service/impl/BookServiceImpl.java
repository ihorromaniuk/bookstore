package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.repository.BookRepository;
import core.basesyntax.bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}

