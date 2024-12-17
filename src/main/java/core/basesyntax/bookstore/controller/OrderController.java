package core.basesyntax.bookstore.controller;

import core.basesyntax.bookstore.dto.exception.ExceptionDto;
import core.basesyntax.bookstore.dto.exception.ValidationExceptionDto;
import core.basesyntax.bookstore.dto.order.CreateOrderRequestDto;
import core.basesyntax.bookstore.dto.order.OrderDto;
import core.basesyntax.bookstore.dto.order.OrderItemDto;
import core.basesyntax.bookstore.dto.order.UpdateOrderStatusRequestDto;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Order management")
@RequiredArgsConstructor
public class OrderController {
    private static final char PATH_SEPARATOR = '/';
    private final OrderService orderService;

    @Operation(summary = "Get order history for user")
    @ApiResponse(
            responseCode = "200",
            description = "Receive order history"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<OrderDto> getOrderHistory(@PageableDefault @ParameterObject Pageable pageable,
                                          Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrders(pageable, user);
    }

    @Operation(summary = "Create order",
            description = "Creates order based on items in users' shopping cart, "
                    + "leaving shopping cart empty")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created new order",
                    content = @Content(schema = @Schema(
                            implementation = OrderDto.class)
                    )
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
                    description = "No items in shopping cart",
                    content = @Content(schema = @Schema(
                            implementation = ExceptionDto.class
                    ))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody @Valid CreateOrderRequestDto requestDto,
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

    @Operation(summary = "Get order by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the order",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the order",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable Long orderId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderById(orderId, user);
    }

    @Operation(summary = "Get order items by order id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found order items",
                    content = @Content(array =
                    @ArraySchema(schema = @Schema(implementation = OrderItemDto.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the order",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    public Page<OrderItemDto> getOrderItems(@PathVariable Long orderId,
                                            @ParameterObject @PageableDefault Pageable pageable,
                                            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItems(orderId, pageable, user);
    }

    @Operation(summary = "Get order item by order id and item id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found order item",
                    content = @Content(schema = @Schema(implementation = OrderItemDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the order or its' item",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{orderItemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId,
                                         @PathVariable Long orderItemId,
                                         Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemById(orderId, orderItemId, user);
    }

    @Operation(summary = "Update order status by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated order status",
                    content = @Content(schema =
                    @Schema(implementation = OrderDto.class))
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
                    description = "Didn't find the order",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    public OrderDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.changeStatus(orderId, requestDto);
    }
}
