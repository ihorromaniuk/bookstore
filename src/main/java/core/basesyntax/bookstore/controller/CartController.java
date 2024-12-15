package core.basesyntax.bookstore.controller;

import core.basesyntax.bookstore.dto.cart.AddCardItemRequestDto;
import core.basesyntax.bookstore.dto.cart.CartItemDto;
import core.basesyntax.bookstore.dto.cart.CartItemWithoutBookDto;
import core.basesyntax.bookstore.dto.cart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.cart.UpdateCartItemQuantityRequestDto;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.getShoppingCart(user);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public CartItemWithoutBookDto addItemToShoppingCart(@RequestBody
                                                            AddCardItemRequestDto requestDto,
                                                        Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.addItemToCart(requestDto, user);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    public CartItemDto updateCartItemQuantity(@PathVariable Long cartItemId,
                                              @RequestBody
                                              UpdateCartItemQuantityRequestDto requestDto,
                                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.updateCartQuantity(cartItemId, requestDto, user);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{cartItemId}")
    public void deleteItem(@PathVariable Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cartService.deleteCartItem(cartItemId, user);
    }
}
