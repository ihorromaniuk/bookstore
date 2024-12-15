package core.basesyntax.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCardItemRequestDto(@NotNull Long bookId,
                                    @Min(1) int quantity) {
}
