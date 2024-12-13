package core.basesyntax.bookstore.repository.book;

import core.basesyntax.bookstore.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    @Override
    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(Long id);

    @Query("from Book b left join fetch b.categories c where c.name = :categoryName")
    List<Book> findAllByCategoryName(String categoryName);

    @Query("from Book b left join fetch b.categories c where c.id in :categoryIds")
    List<Book> findAllByCategoryIds(List<Long> categoryIds);
}
