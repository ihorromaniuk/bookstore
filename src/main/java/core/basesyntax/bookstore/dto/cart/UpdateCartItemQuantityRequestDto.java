package core.basesyntax.bookstore.dto.cart;

import jakarta.validation.constraints.Min;

public record UpdateCartItemQuantityRequestDto(@Min(1) int quantity) {
}
