package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.cart.ShoppingCartDto;
import core.basesyntax.bookstore.dto.order.OrderDto;
import core.basesyntax.bookstore.dto.order.OrderItemDto;
import core.basesyntax.bookstore.dto.order.OrderWithoutItemsDto;
import core.basesyntax.bookstore.mapper.OrderMapper;
import core.basesyntax.bookstore.model.Order;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.repository.order.OrderItemRepository;
import core.basesyntax.bookstore.repository.order.OrderRepository;
import core.basesyntax.bookstore.service.CartService;
import core.basesyntax.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderWithoutItemsDto> getOrders(Pageable pageable, User user) {
        return orderRepository.findAllByUser(user, pageable)
                .map(orderMapper::toOrderWithoutItemsDto);
    }

    @Override
    public OrderDto createOrder(String shippingAddress, User user) {
        ShoppingCartDto shoppingCart = cartService.getShoppingCart(user);
//        if (shoppingCart.cartItems().isEmpty()) {
//            throw new
//        }
//        TODO: continue
//        orderMapper.orderItemFromCartItem(shoppingCart.cartItems());
        return null;
    }

    @Override
    public Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable, User user) {
        return null;
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long orderItemId, User user) {
        return null;
    }

    @Override
    public OrderDto changeStatus(Long orderId, Order.Status status, User user) {
        return null;
    }
}
