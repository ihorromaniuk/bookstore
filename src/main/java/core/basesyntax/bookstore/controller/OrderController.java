package core.basesyntax.bookstore.controller;

import core.basesyntax.bookstore.dto.order.CreateOrderRequestDto;
import core.basesyntax.bookstore.dto.order.OrderDto;
import core.basesyntax.bookstore.dto.order.OrderItemDto;
import core.basesyntax.bookstore.dto.order.UpdateOrderStatusRequestDto;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final char PATH_SEPARATOR = '/';
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<OrderDto> getOrderHistory(@PageableDefault Pageable pageable,
                                          Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrders(pageable, user);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid CreateOrderRequestDto requestDto,
                                                Authentication authentication,
                                                HttpServletRequest request) {
        User user = (User) authentication.getPrincipal();
        OrderDto orderDto = orderService.createOrder(requestDto, user);
        return ResponseEntity.created(
                URI.create(request.getRequestURL()
                        .append(PATH_SEPARATOR)
                        .append(orderDto.id())
                        .toString())
        ).body(orderDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable Long orderId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderById(orderId, user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    public Page<OrderItemDto> getOrderItems(@PathVariable Long orderId,
                                            @ParameterObject @PageableDefault Pageable pageable,
                                            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItems(orderId, pageable, user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{orderItemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId,
                                         @PathVariable Long orderItemId,
                                         Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemById(orderId, orderItemId, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    public OrderDto updateOrderStatus(@PathVariable Long orderId,
                                      @RequestBody @Valid
                                      UpdateOrderStatusRequestDto requestDto) {
        return orderService.changeStatus(orderId, requestDto);
    }
}
