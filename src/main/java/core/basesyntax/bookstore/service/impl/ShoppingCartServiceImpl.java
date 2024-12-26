package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.shoppingcart.AddCardItemRequestDto;
import core.basesyntax.bookstore.dto.shoppingcart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.shoppingcart.UpdateCartItemQuantityRequestDto;
import core.basesyntax.bookstore.exception.EntityNotFoundException;
import core.basesyntax.bookstore.mapper.ShoppingCartMapper;
import core.basesyntax.bookstore.model.CartItem;
import core.basesyntax.bookstore.model.ShoppingCart;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.repository.book.BookRepository;
import core.basesyntax.bookstore.repository.shoppingcart.CartItemRepository;
import core.basesyntax.bookstore.repository.shoppingcart.ShoppingCartRepository;
import core.basesyntax.bookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper cartMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getShoppingCart(User user) {
        return cartMapper.toShoppingCartDto(shoppingCartRepository.getByUser(user));
    }

    @Override
    public ShoppingCartDto addItemToCart(AddCardItemRequestDto requestDto, User user) {
        if (!bookRepository.existsById(requestDto.bookId())) {
            throw new EntityNotFoundException("Can't find book by id. "
                    + "Id: " + requestDto.bookId());
        }

        CartItem cartItem = cartMapper.toModel(requestDto);
        ShoppingCart shoppingCart = shoppingCartRepository.getByUser(user);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);

        return cartMapper.toShoppingCartDto(shoppingCartRepository.getByUser(user));
    }

    @Override
    public ShoppingCartDto updateCartQuantity(Long cartItemId,
                                                       UpdateCartItemQuantityRequestDto requestDto,
                                                       User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.getByUser(user);
        CartItem cartItem = shoppingCart
                .getCartItems()
                .stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find cart item by id. "
                                + "Id: " + cartItemId));
        cartItem.setQuantity(requestDto.quantity());
        cartItemRepository.save(cartItem);
        return cartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public void deleteCartItem(Long cartItemId, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.getByUser(user);
        if (!cartItemRepository.existsByIdAndShoppingCart(cartItemId, shoppingCart)) {
            throw new EntityNotFoundException("Can't find cart item by id. Id: " + cartItemId);
        }

        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
