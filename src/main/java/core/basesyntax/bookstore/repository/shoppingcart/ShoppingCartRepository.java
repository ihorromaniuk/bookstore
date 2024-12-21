package core.basesyntax.bookstore.repository.shoppingcart;

import core.basesyntax.bookstore.model.ShoppingCart;
import core.basesyntax.bookstore.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    ShoppingCart getByUser(User user);
}
