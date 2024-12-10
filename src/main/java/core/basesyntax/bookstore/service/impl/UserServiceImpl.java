package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.user.UserDto;
import core.basesyntax.bookstore.dto.user.UserRegistrationRequestDto;
import core.basesyntax.bookstore.exception.RegistrationException;
import core.basesyntax.bookstore.mapper.UserMapper;
import core.basesyntax.bookstore.repository.user.UserRepository;
import core.basesyntax.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public UserDto save(UserRegistrationRequestDto requestDto) {
        if (repository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("Email already registered. "
                    + "Email: " + requestDto.email());
        }

        return userMapper.toDto(repository.save(userMapper.toModel(requestDto)));
    }
}
