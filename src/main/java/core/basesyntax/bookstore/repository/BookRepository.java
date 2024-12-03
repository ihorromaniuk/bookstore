package core.basesyntax.bookstore.repository;

import core.basesyntax.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
