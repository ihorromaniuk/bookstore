package core.basesyntax.bookstore.dto.cart;

public record AddCardItemRequestDto(Long bookId,
                                    int quantity) {
}
