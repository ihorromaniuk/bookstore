package core.basesyntax.bookstore.exception;

public class BeanNotFoundException extends RuntimeException {
    public BeanNotFoundException(String message) {
        super(message);
    }
}
