package uk.uclan.donationsplatform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.uclan.donationsplatform.beans.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByAuthority(String authority);
}
