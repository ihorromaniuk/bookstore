package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.user.UserDto;
import core.basesyntax.bookstore.dto.user.UserRegistrationRequestDto;

public interface UserService {
    UserDto save(UserRegistrationRequestDto requestDto);
}
