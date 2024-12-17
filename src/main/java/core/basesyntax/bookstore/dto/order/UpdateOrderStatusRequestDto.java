package core.basesyntax.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatusRequestDto(@NotBlank
                                          String status) {
}
