package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.user.UserDto;
import core.basesyntax.bookstore.dto.user.UserRegistrationRequestDto;
import core.basesyntax.bookstore.exception.RegistrationException;
import core.basesyntax.bookstore.mapper.UserMapper;
import core.basesyntax.bookstore.model.Role;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.repository.user.RoleRepository;
import core.basesyntax.bookstore.repository.user.UserRepository;
import core.basesyntax.bookstore.service.UserService;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto save(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("Email already registered. "
                    + "Email: " + requestDto.email());
        }

        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        Role userRole = roleRepository.getFirstByRole(Role.RoleName.USER);
        user.setRoles(Set.of(userRole));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
