package core.basesyntax.bookstore.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.model.Category;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = {
            "classpath:database/category/insert_category.sql",
            "classpath:database/book/insert_mr_mercedes_book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_mr_mercedes_book.sql",
            "classpath:database/category/remove_all_categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_booksWithCategoriesNoLazyInitializationThrown_ok() {
        Long id = 4L;
        Book book = bookRepository.findById(id).orElseThrow();
        Set<Category> categories = book.getCategories();

        assertEquals(1, categories.size());
    }

    @Test
    @Sql(scripts = {
            "classpath:database/category/insert_2_categories.sql",
            "classpath:database/book/insert_3_books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_all_books.sql",
            "classpath:database/category/remove_all_categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesContain_findBooksByCategory_ok() {
        Pageable pageable = Pageable.unpaged();
        Category category = new Category(2L);
        Page<Book> page = bookRepository.findAllByCategoriesContains(Set.of(category), pageable);

        List<Book> books = page.toList();
        assertEquals(2, books.size());

        Book hobbit = books.stream()
                .filter(book -> book.getId() == 1)
                .findFirst().orElseThrow();
        assertEquals("Hobbit", hobbit.getTitle());
        assertEquals("J.R.R. Tolkien", hobbit.getAuthor());

        Book harryPotter = books.stream()
                .filter(book -> book.getId() == 2)
                .findFirst().orElseThrow();
        assertEquals("Harry Potter and the Philosopher's stone", harryPotter.getTitle());
        assertEquals("J.K. Rowling", harryPotter.getAuthor());
    }
}
