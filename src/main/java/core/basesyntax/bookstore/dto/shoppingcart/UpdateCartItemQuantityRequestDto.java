package core.basesyntax.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemQuantityRequestDto(@Positive int quantity) {
}
