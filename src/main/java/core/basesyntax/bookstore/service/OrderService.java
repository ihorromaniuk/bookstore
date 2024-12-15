package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.order.OrderDto;
import core.basesyntax.bookstore.dto.order.OrderItemDto;
import core.basesyntax.bookstore.dto.order.OrderWithoutItemsDto;
import core.basesyntax.bookstore.model.CartItem;
import core.basesyntax.bookstore.model.Order;
import core.basesyntax.bookstore.model.OrderItem;
import core.basesyntax.bookstore.model.User;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderWithoutItemsDto> getOrders(Pageable pageable, User user);

    OrderDto createOrder(String shippingAddress, User user);

    Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable, User user);

    OrderItemDto getOrderItemById(Long orderId, Long orderItemId, User user);

    OrderDto changeStatus(Long orderId, Order.Status status, User user);
}
