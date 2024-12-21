package core.basesyntax.bookstore.repository.shoppingcart;

import core.basesyntax.bookstore.model.CartItem;
import core.basesyntax.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByIdAndShoppingCart(Long id, ShoppingCart shoppingCart);
}
