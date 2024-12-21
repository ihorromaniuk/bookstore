package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.shoppingcart.AddCardItemRequestDto;
import core.basesyntax.bookstore.dto.shoppingcart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.shoppingcart.UpdateCartItemQuantityRequestDto;
import core.basesyntax.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(User user);

    ShoppingCartDto addItemToCart(AddCardItemRequestDto requestDto, User user);

    ShoppingCartDto updateCartQuantity(Long cartItemId,
                                                UpdateCartItemQuantityRequestDto requestDto,
                                                User user);

    void deleteCartItem(Long cartItemId, User user);

    void createShoppingCart(User user);
}
