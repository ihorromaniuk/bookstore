package core.basesyntax.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record CreateBookRequestDto(@NotBlank String title,
                                   @NotBlank String author,
                                   @NotBlank String isbn,
                                   @NotNull BigDecimal price,
                                   String description,
                                   String coverImage,
                                   @NotEmpty List<Long> categoryIds) {
}
