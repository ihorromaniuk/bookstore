package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.user.UserDto;
import core.basesyntax.bookstore.dto.user.UserRegistrationRequestDto;
import core.basesyntax.bookstore.model.User;
import java.util.Optional;

public interface UserService {
    UserDto save(UserRegistrationRequestDto requestDto);

    Optional<User> findByEmail(String email);
}
