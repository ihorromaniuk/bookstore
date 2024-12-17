package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.cart.AddCardItemRequestDto;
import core.basesyntax.bookstore.dto.cart.CartItemDto;
import core.basesyntax.bookstore.dto.cart.CartItemWithBookTitleDto;
import core.basesyntax.bookstore.dto.cart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.cart.UpdateCartItemQuantityRequestDto;
import core.basesyntax.bookstore.model.User;

public interface CartService {
    ShoppingCartDto getShoppingCart(User user);

    CartItemDto addItemToCart(AddCardItemRequestDto requestDto, User user);

    CartItemWithBookTitleDto updateCartQuantity(Long cartItemId,
                                                UpdateCartItemQuantityRequestDto requestDto,
                                                User user);

    void deleteCartItem(Long cartItemId, User user);
}
