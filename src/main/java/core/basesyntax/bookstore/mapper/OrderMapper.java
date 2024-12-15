package core.basesyntax.bookstore.mapper;

import core.basesyntax.bookstore.config.MapperConfig;
import core.basesyntax.bookstore.dto.order.OrderDto;
import core.basesyntax.bookstore.dto.order.OrderItemDto;
import core.basesyntax.bookstore.dto.order.OrderWithoutItemsDto;
import core.basesyntax.bookstore.model.CartItem;
import core.basesyntax.bookstore.model.Order;
import core.basesyntax.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);

    @Mapping(target = "userId", source = "user.id")
    OrderWithoutItemsDto toOrderWithoutItemsDto(Order order);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toOrderItemDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "price", ignore = true)
    void orderItemFromCartItem(@MappingTarget OrderItem orderItem, CartItem cartItem);
}
