package core.basesyntax.bookstore.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddCardItemRequestDto(@NotNull Long bookId,
                                    @Positive int quantity) {
}
