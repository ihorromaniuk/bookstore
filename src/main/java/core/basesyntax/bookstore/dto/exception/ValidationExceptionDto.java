package core.basesyntax.bookstore.dto.exception;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ValidationExceptionDto(HttpStatus status,
                                      LocalDateTime timestamp,
                                      List<String> messages) {
    public ValidationExceptionDto(HttpStatus status, List<String> messages) {
        this(status, LocalDateTime.now(), messages);
    }
}
