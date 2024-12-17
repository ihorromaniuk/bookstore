package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.cart.AddCardItemRequestDto;
import core.basesyntax.bookstore.dto.cart.CartItemDto;
import core.basesyntax.bookstore.dto.cart.CartItemWithBookTitleDto;
import core.basesyntax.bookstore.dto.cart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.cart.UpdateCartItemQuantityRequestDto;
import core.basesyntax.bookstore.exception.EntityNotFoundException;
import core.basesyntax.bookstore.mapper.CartMapper;
import core.basesyntax.bookstore.model.CartItem;
import core.basesyntax.bookstore.model.ShoppingCart;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.repository.cart.CartItemRepository;
import core.basesyntax.bookstore.repository.cart.ShoppingCartRepository;
import core.basesyntax.bookstore.service.BookService;
import core.basesyntax.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final BookService bookService;

    @Override
    public ShoppingCartDto getShoppingCart(User user) {
        return cartMapper.toShoppingCartDto(shoppingCartRepository.getByUser(user));
    }

    @Override
    public CartItemDto addItemToCart(AddCardItemRequestDto requestDto, User user) {
        if (!bookService.existsById(requestDto.bookId())) {
            throw new EntityNotFoundException("Can't find book by id. "
                    + "Id: " + requestDto.bookId());
        }
        CartItem item = cartMapper.toModel(requestDto);
        item.setShoppingCart(shoppingCartRepository.getByUser(user));
        return cartMapper.toCartItemDto(cartItemRepository.save(item));
    }

    @Override
    public CartItemWithBookTitleDto updateCartQuantity(Long cartItemId,
                                                       UpdateCartItemQuantityRequestDto requestDto,
                                                       User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.getByUser(user);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCart(cartItemId, shoppingCart)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find cart item by id. "
                                + "Id: " + cartItemId));
        cartMapper.updateCartItemFromDto(cartItem, requestDto);
        cartItemRepository.save(cartItem);
        return cartMapper.toCartItemWithBookTitleDto(cartItem);
    }

    @Override
    public void deleteCartItem(Long cartItemId, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.getByUser(user);
        if (cartItemRepository.existsByIdAndShoppingCart(cartItemId, shoppingCart)) {
            cartItemRepository.deleteById(cartItemId);
        }

        throw new EntityNotFoundException("Can't find cart item by id. Id: " + cartItemId);
    }
}
