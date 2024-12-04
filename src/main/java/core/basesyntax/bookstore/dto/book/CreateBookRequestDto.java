package core.basesyntax.bookstore.dto.book;

import core.basesyntax.bookstore.model.Book;
import java.math.BigDecimal;

/**
 * DTO for {@link Book}
 */
public record CreateBookRequestDto(String title, String author, String isbn,
                                   BigDecimal price, String description,
                                   String coverImage) {
}
