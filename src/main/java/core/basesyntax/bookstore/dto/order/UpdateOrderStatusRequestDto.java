package core.basesyntax.bookstore.dto.order;

import core.basesyntax.bookstore.annotation.enumvalue.ValueOfEnum;
import core.basesyntax.bookstore.model.Order;

public record UpdateOrderStatusRequestDto(
        @ValueOfEnum(enumClass = Order.Status.class) String status) {
}
