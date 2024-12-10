package core.basesyntax.bookstore.exception;

import core.basesyntax.bookstore.dto.book.ExceptionDto;
import core.basesyntax.bookstore.dto.book.ValidationExceptionDto;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionDto exceptionDto = new ExceptionDto(status, ex.getMessage());
        return new ResponseEntity<>(exceptionDto, status);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ExceptionDto> handleRegistrationException(Exception ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionDto exceptionDto = new ExceptionDto(status, ex.getMessage());
        return new ResponseEntity<>(exceptionDto, status);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionDto> handleAuthorizationDeniedException(
            AuthorizationDeniedException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ExceptionDto exceptionDto = new ExceptionDto(status, ex.getMessage());
        return new ResponseEntity<>(exceptionDto, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        ValidationExceptionDto exceptionDto = new ValidationExceptionDto(httpStatus, errors);
        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            return field + " " + message;
        }
        return error.getDefaultMessage();
    }
}
