package core.basesyntax.bookstore.dto.shoppingcart;

public record CartItemDto(Long id,
                          Long bookId,
                          String bookTitle,
                          int quantity) {
}
