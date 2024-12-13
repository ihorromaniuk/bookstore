package core.basesyntax.bookstore.dto.cart;

public record CartItemWithoutBookDto(Long bookId,
                                     int quantity) {
}
