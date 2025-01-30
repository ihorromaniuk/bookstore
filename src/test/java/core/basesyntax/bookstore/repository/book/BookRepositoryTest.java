package core.basesyntax.bookstore.repository.book;

import static core.basesyntax.bookstore.util.TestUtil.getDbHarryPotterBook;
import static core.basesyntax.bookstore.util.TestUtil.getDbHobbitBook;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.model.Category;
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
            "classpath:database/category/insert_detective_category.sql",
            "classpath:database/book/insert_bot_book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_all_books.sql",
            "classpath:database/category/remove_all_categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_booksWithCategoriesNoLazyInitializationThrown_ok() {
        Long id = 1L;
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

        Book[] books = page.toList().toArray(new Book[0]);
        assertEquals(2, books.length);

        assertThat(books)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("categories")
                .isEqualTo(new Book[]{getDbHobbitBook(), getDbHarryPotterBook()});
    }
}
