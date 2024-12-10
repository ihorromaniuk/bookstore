package core.basesyntax.bookstore.repository.user;

import core.basesyntax.bookstore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("from User u left join fetch u.roles where u.email = :email")
    Optional<User> findByEmail(String email);
}
