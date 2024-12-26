package core.basesyntax.bookstore.dto.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;

public record ValidationExceptionDto(HttpStatus status,
                                      LocalDateTime timestamp,
                                      Set<String> messages) {
    public ValidationExceptionDto(HttpStatus status, Set<String> messages) {
        this(status, LocalDateTime.now(), messages);
    }
}
