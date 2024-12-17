package core.basesyntax.bookstore.repository.order;

import core.basesyntax.bookstore.model.Order;
import core.basesyntax.bookstore.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(User user, Pageable pageable);

    boolean existsByIdAndUser(Long id, User user);

    Optional<Order> findByIdAndUser(Long id, User user);
}
