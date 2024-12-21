package core.basesyntax.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddCardItemRequestDto(@NotNull @Positive Long bookId,
                                    @Positive int quantity) {
}
