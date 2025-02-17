package core.basesyntax.bookstore.controller;

import core.basesyntax.bookstore.dto.exception.ExceptionDto;
import core.basesyntax.bookstore.dto.exception.ValidationExceptionDto;
import core.basesyntax.bookstore.dto.shoppingcart.AddCardItemRequestDto;
import core.basesyntax.bookstore.dto.shoppingcart.CartItemDto;
import core.basesyntax.bookstore.dto.shoppingcart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.shoppingcart.UpdateCartItemQuantityRequestDto;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Shopping cart management",
        description = "Endpoints to manage shopping cart for currently logged in user")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart")
    @ApiResponse(
            responseCode = "200",
            description = "Receive shopping cart info and its' items"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user);
    }

    @Operation(summary = "Add cart item")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Added cart item to shopping cart",
                    content = @Content(schema =
                    @Schema(implementation = ShoppingCartDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(schema = @Schema(
                            implementation = ValidationExceptionDto.class
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the book by id",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ShoppingCartDto addItemToShoppingCart(
            @RequestBody @Valid AddCardItemRequestDto requestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addItemToCart(requestDto, user);
    }

    @Operation(summary = "Update cart item quantity")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated cart item quantity",
                    content = @Content(schema =
                    @Schema(implementation = CartItemDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(schema = @Schema(
                            implementation = ValidationExceptionDto.class
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find cart item by id",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemQuantityRequestDto requestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartQuantity(cartItemId, requestDto, user);
    }

    @Operation(summary = "Delete cart item")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Deleted cart item"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find cart item by id",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{cartItemId}")
    public void deleteItem(@PathVariable Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteCartItem(cartItemId, user);
    }
}
