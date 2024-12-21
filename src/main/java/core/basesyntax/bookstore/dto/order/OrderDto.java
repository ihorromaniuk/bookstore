package core.basesyntax.bookstore.dto.order;

import core.basesyntax.bookstore.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(Long id,
                       Order.Status status,
                       BigDecimal total,
                       LocalDateTime orderDate,
                       Long userId) {
}
