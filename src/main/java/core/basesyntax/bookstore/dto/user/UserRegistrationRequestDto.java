package core.basesyntax.bookstore.dto.user;

import core.basesyntax.bookstore.annotation.fieldmatch.FieldsMatch;
import jakarta.validation.constraints.NotBlank;

@FieldsMatch(field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords don't match")
public record UserRegistrationRequestDto(@NotBlank String email,
                                         @NotBlank String password,
                                         @NotBlank String repeatPassword,
                                         @NotBlank String firstName,
                                         @NotBlank String lastName,
                                         String shippingAddress) {
}
