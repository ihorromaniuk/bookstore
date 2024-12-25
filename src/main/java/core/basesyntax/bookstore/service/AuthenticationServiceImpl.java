package core.basesyntax.bookstore.service;

import core.basesyntax.bookstore.dto.user.UserLoginRequestDto;
import core.basesyntax.bookstore.dto.user.UserLoginResponseDto;
import core.basesyntax.bookstore.security.JwtUtil;
import core.basesyntax.bookstore.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager manager;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = manager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.email(), requestDto.password()));
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
