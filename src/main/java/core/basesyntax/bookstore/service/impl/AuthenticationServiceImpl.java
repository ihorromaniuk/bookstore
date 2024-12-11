package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.user.UserLoginRequestDto;
import core.basesyntax.bookstore.dto.user.UserLoginResponseDto;
import core.basesyntax.bookstore.exception.AuthenticationException;
import core.basesyntax.bookstore.model.User;
import core.basesyntax.bookstore.repository.user.UserRepository;
import core.basesyntax.bookstore.security.JwtUtil;
import core.basesyntax.bookstore.service.AuthenticationService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        Optional<User> userOptional = userRepository.findByEmail(requestDto.email());
        if (userOptional.isPresent()
                && passwordEncoder
                .matches(requestDto.password(), userOptional.get().getPassword())) {
            return new UserLoginResponseDto(jwtUtil.generateToken(userOptional.get()));
        }

        throw new AuthenticationException("Authentication failed");
    }
}
