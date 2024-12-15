package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.cart.AddCardItemRequestDto;
import core.basesyntax.bookstore.dto.cart.CartItemDto;
import core.basesyntax.bookstore.dto.cart.CartItemWithoutBookDto;
import core.basesyntax.bookstore.dto.cart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.cart.UpdateCartItemQuantityRequestDto;
import core.basesyntax.bookstore.model.User;

public interface CartService {
    ShoppingCartDto getShoppingCart(User user);

    CartItemWithoutBookDto addItemToCart(AddCardItemRequestDto requestDto, User user);

    CartItemDto updateCartQuantity(Long cartItemId,
                                   UpdateCartItemQuantityRequestDto requestDto, User user);

    void deleteCartItem(Long cartItemId, User user);
}
