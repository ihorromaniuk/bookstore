package core.basesyntax.bookstore.dto.order;

import core.basesyntax.bookstore.annotation.enumvalue.ValueOfEnum;
import core.basesyntax.bookstore.model.Order;
import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatusRequestDto(
        @ValueOfEnum(enumClass = Order.Status.class) @NotBlank String status) {
}
