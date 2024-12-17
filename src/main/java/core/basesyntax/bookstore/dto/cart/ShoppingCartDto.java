package core.basesyntax.bookstore.dto.cart;

import java.util.List;

public record ShoppingCartDto(Long id,
                              Long userId,
                              List<CartItemWithBookTitleDto> cartItems) {
}
