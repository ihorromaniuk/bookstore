package core.basesyntax.bookstore.dto.order;

import core.basesyntax.bookstore.model.Order;
import core.basesyntax.bookstore.model.OrderItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(Long id,
                       Order.Status status,
                       BigDecimal total,
                       LocalDateTime orderDate,
                       Long userId,
                       Set<OrderItem> orderItems) {
}
