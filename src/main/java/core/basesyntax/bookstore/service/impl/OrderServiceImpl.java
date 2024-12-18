package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.order.CreateOrderRequestDto;
import core.basesyntax.bookstore.dto.order.OrderDto;
import core.basesyntax.bookstore.dto.order.OrderItemDto;
import core.basesyntax.bookstore.dto.order.UpdateOrderStatusRequestDto;
import core.basesyntax.bookstore.exception.EntityNotFoundException;
import core.basesyntax.bookstore.exception.ItemsNotFoundException;
import core.basesyntax.bookstore.mapper.OrderMapper;
import core.basesyntax.bookstore.model.Order;
import core.basesyntax.bookstore.model.OrderItem;
import core.basesyntax.bookstore.model.ShoppingCart;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.repository.cart.ShoppingCartRepository;
import core.basesyntax.bookstore.repository.order.OrderItemRepository;
import core.basesyntax.bookstore.repository.order.OrderRepository;
import core.basesyntax.bookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderDto> getOrders(Pageable pageable, User user) {
        return orderRepository.findAllByUser(user, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.getByUser(user);
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new ItemsNotFoundException("No items found in shopping cart to create order");
        }
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(orderMapper::orderItemFromCartItem)
                .collect(Collectors.toSet());
        shoppingCart.clear();
        shoppingCartRepository.save(shoppingCart);

        BigDecimal total = orderItems.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setShippingAddress(requestDto.shippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.CREATED);
        order.setOrderItems(orderItems);
        order.setTotal(total);
        order.setUser(user);

        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto getOrderById(Long orderId, User user) {
        return orderMapper.toDto(orderRepository.findByIdAndUser(orderId, user).orElseThrow(() ->
                new EntityNotFoundException("Can't find order by id. Id: " + orderId)));
    }

    @Override
    public Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable, User user) {
        Order order = orderRepository.findByIdAndUser(orderId, user).orElseThrow(() ->
                new EntityNotFoundException("Can't find order by id. Id: " + orderId));
        Page<OrderItem> orderItems = orderItemRepository
                .findAllByOrder(order, pageable);
        List<OrderItemDto> orderItemDtos = orderItems
                .stream()
                .map(orderMapper::toOrderItemDto)
                .toList();
        return new PageImpl<>(orderItemDtos, pageable, orderItems.getTotalElements());
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long orderItemId, User user) {
        return orderMapper.toOrderItemDto(orderItemRepository
                .findByIdAndOrder_IdAndOrder_User(orderItemId, orderId, user).orElseThrow(() ->
                        new EntityNotFoundException("Can't find order item by id. "
                                + "Order id: " + orderId
                                + ", order item id" + orderItemId)));
    }

    @Override
    public OrderDto changeStatus(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id. "
                        + "Id: " + orderId));
        order.setStatus(Order.Status.valueOf(requestDto.status()));
        return orderMapper.toDto(orderRepository.save(order));
    }
}
