package core.basesyntax.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateBookRequestDto(@NotBlank String title,
                                   @NotNull String author,
                                   @NotNull String isbn,
                                   @NotNull BigDecimal price,
                                   String description,
                                   String coverImage) {
}
