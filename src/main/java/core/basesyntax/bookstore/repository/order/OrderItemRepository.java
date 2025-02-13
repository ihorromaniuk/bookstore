package core.basesyntax.bookstore.repository.order;

import core.basesyntax.bookstore.model.Order;
import core.basesyntax.bookstore.model.OrderItem;
import core.basesyntax.bookstore.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findAllByOrder(Order order, Pageable pageable);

    Optional<OrderItem> findByIdAndOrder_IdAndOrder_User(Long id, Long orderId, User user);
}
