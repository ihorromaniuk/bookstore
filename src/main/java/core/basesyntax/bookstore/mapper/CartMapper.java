package core.basesyntax.bookstore.mapper;

import core.basesyntax.bookstore.config.MapperConfig;
import core.basesyntax.bookstore.dto.cart.AddCardItemRequestDto;
import core.basesyntax.bookstore.dto.cart.CartItemDto;
import core.basesyntax.bookstore.dto.cart.CartItemWithoutBookDto;
import core.basesyntax.bookstore.dto.cart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.cart.UpdateCartItemQuantityRequestDto;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.model.CartItem;
import core.basesyntax.bookstore.model.ShoppingCart;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartMapper {
    @Mapping(target = "book", source = "bookId", qualifiedByName = "setBook")
    CartItem toModel(AddCardItemRequestDto dto);

    @Named("setBook")
    default Book setBook(Long bookId) {
        return new Book(bookId);
    }

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toCartItemDto(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    CartItemWithoutBookDto toCartItemWithoutDto(CartItem cartItem);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems", qualifiedByName = "setShoppingCarts")
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);

    @Named("setShoppingCarts")
    default List<CartItemDto> setShoppingCarts(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toCartItemDto)
                .toList();
    }

    void updateCartItemFromDto(@MappingTarget CartItem cartItem,
                               UpdateCartItemQuantityRequestDto dto);
}
