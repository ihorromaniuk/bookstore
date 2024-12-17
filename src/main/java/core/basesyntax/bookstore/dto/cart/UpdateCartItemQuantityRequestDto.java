package core.basesyntax.bookstore.dto.cart;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemQuantityRequestDto(@Positive int quantity) {
}
