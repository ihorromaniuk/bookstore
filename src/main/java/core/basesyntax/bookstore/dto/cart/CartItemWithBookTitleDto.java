package core.basesyntax.bookstore.dto.cart;

public record CartItemWithBookTitleDto(Long id,
                                       Long bookId,
                                       String bookTitle,
                                       int quantity) {
}
