package core.basesyntax.bookstore.repository.shoppingcart;

import core.basesyntax.bookstore.model.CartItem;
import core.basesyntax.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByIdAndShoppingCart(Long id, ShoppingCart shoppingCart);

    @EntityGraph(attributePaths = "book")
    Optional<CartItem> findByIdAndShoppingCart(Long id, ShoppingCart shoppingCart);
}
