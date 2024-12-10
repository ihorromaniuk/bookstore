package core.basesyntax.bookstore.repository.user;

import core.basesyntax.bookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getFirstByRole(Role.RoleName role);
}
