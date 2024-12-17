package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.order.CreateOrderRequestDto;
import core.basesyntax.bookstore.dto.order.OrderDto;
import core.basesyntax.bookstore.dto.order.OrderItemDto;
import core.basesyntax.bookstore.dto.order.UpdateOrderStatusRequestDto;
import core.basesyntax.bookstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getOrders(Pageable pageable, User user);

    OrderDto createOrder(CreateOrderRequestDto requestDto, User user);

    OrderDto getOrderById(Long orderId, User user);

    Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable, User user);

    OrderItemDto getOrderItemById(Long orderId, Long orderItemId, User user);

    OrderDto changeStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);
}
