package core.basesyntax.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequestDto(@NotBlank String name,
                                       String description) {
}
