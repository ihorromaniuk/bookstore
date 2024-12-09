package core.basesyntax.bookstore.dto.user;

import core.basesyntax.bookstore.annotation.fieldmatch.FieldsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldsMatch(field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords don't match")
public record UserRegistrationRequestDto(@NotBlank String email,
                                         @Size(min = 8, max = 24)
                                         @NotBlank String password,
                                         @NotBlank String repeatPassword,
                                         @NotBlank String firstName,
                                         @NotBlank String lastName,
                                         String shippingAddress) {
}
