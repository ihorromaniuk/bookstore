package core.basesyntax.bookstore.util;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.category.CategoryDto;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TestUtil {
    public static final Long ID_AFTER_LAST_INSERTED_BOOK = 4L;
    public static final Long ID = 1L;
    public static final String TITLE = "Shining";
    public static final String AUTHOR = "Stephen King";
    public static final BigDecimal PRICE = BigDecimal.valueOf(100);
    public static final String ISBN = "12345678";
    public static final String DESCRIPTION = "spooky book";
    public static final String COVER_IMAGE = "www.cool-images.com/shining";
    public static final String CONTENT_KEY = "content";

    public static final Long ID_AFTER_LAST_INSERTED_CATEGORY = 3L;
    public static final Long ID_SCI_FI = 1L;
    public static final String NAME_SCI_FI = "sci-fi";
    public static final String DESCRIPTION_SCI_FI = "sci-fi genre";
    public static final Long ID_ACTION = 2L;
    public static final String NAME_ACTION = "action";
    public static final String DESCRIPTION_ACTION = "action genre";

    private static final BookDto DB_HARRY_POTTER_BOOK_DTO = new BookDto(
            2L,
            "Harry Potter and the Philosopher's stone",
            "J.K. Rowling",
            "9781408855652",
            BigDecimal.valueOf(475),
            "Book about wizard",
            "image url",
            Set.of(2L)
    );

    private static final BookDto SHINING_BOOK_DTO = new BookDto(
            ID,
            TITLE,
            AUTHOR,
            ISBN,
            PRICE,
            DESCRIPTION,
            COVER_IMAGE,
            Set.of(ID)
    );

    private static final Book DB_HOBBIT_BOOK = new Book(1L)
            .setTitle("Hobbit")
            .setAuthor("J.R.R. Tolkien")
            .setIsbn("9780261102217")
            .setPrice(BigDecimal.valueOf(500))
            .setDescription("Book about adventures of hobbit")
            .setCoverImage("image url")
            .setDeleted(false);

    private static final Book DB_HARRY_POTTER_BOOK = new Book(2L)
            .setTitle("Harry Potter and the Philosopher's stone")
            .setAuthor("J.K. Rowling")
            .setIsbn("9781408855652")
            .setPrice(BigDecimal.valueOf(475))
            .setDescription("Book about wizard")
            .setCoverImage("image url")
            .setDeleted(false);

    private static final Book SHINING_BOOK = new Book(ID)
            .setTitle(TITLE)
            .setAuthor(AUTHOR)
            .setIsbn(ISBN)
            .setPrice(PRICE)
            .setDescription(DESCRIPTION)
            .setCoverImage(COVER_IMAGE)
            .setDeleted(false);

    private static final CategoryDto ACTION_DTO =
            new CategoryDto(ID_ACTION, NAME_ACTION, DESCRIPTION_ACTION);

    private static final CategoryDto SCI_FI_DTO =
            new CategoryDto(ID_SCI_FI, NAME_SCI_FI, DESCRIPTION_SCI_FI);

    private static final Category ACTION_CATEGORY = new Category(ID_ACTION)
            .setName(NAME_ACTION)
            .setDescription(DESCRIPTION_ACTION);

    private static final Category SCI_FI_CATEGORY = new Category(ID_SCI_FI)
            .setName(NAME_SCI_FI)
            .setDescription(DESCRIPTION_SCI_FI);

    public static List<BookWithoutCategoryDto> getDbBooksWithoutCategory() {
        BookWithoutCategoryDto hobbit = new BookWithoutCategoryDto(
                1L,
                "Hobbit",
                "J.R.R. Tolkien",
                "9780261102217",
                BigDecimal.valueOf(500),
                "Book about adventures of hobbit",
                "image url"
        );
        BookWithoutCategoryDto harryPotter = new BookWithoutCategoryDto(
                2L,
                "Harry Potter and the Philosopher's stone",
                "J.K. Rowling",
                "9781408855652",
                BigDecimal.valueOf(475),
                "Book about wizard",
                "image url"
        );
        BookWithoutCategoryDto charlie = new BookWithoutCategoryDto(
                3L,
                "Charlie and the Chocolate Factory",
                "Q. Blake",
                "9780241558324",
                BigDecimal.valueOf(400),
                "Book about Willy Wonka",
                "image url"
        );

        return List.of(hobbit, harryPotter, charlie);
    }

    public static BookDto getDbHarryPotterBookDto() {
        return DB_HARRY_POTTER_BOOK_DTO;
    }

    public static BookDto getShiningBookDto() {
        return SHINING_BOOK_DTO;
    }

    public static Book getDbHobbitBook() {
        return DB_HOBBIT_BOOK;
    }

    public static Book getDbHarryPotterBook() {
        return DB_HARRY_POTTER_BOOK;
    }

    public static Book getShiningBook() {
        return SHINING_BOOK;
    }

    public static CategoryDto getActionDto() {
        return ACTION_DTO;
    }

    public static CategoryDto getSciFiDto() {
        return SCI_FI_DTO;
    }

    public static Category getActionCategory() {
        return ACTION_CATEGORY;
    }

    public static Category getSciFiCategory() {
        return SCI_FI_CATEGORY;
    }
}
