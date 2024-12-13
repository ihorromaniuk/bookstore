package core.basesyntax.bookstore.repository.book;

import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.model.Category;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    @Override
    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(Long id);

    Page<Book> findAllByCategoriesContains(Set<Category> categories, Pageable pageable);
}
