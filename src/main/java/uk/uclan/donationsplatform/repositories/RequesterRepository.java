package uk.uclan.donationsplatform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.uclan.donationsplatform.beans.Requester;

import java.util.Optional;

@Repository
public interface RequesterRepository extends JpaRepository<Requester, Integer> {
    Optional<Requester> findByUsernameContainingIgnoreCase(String username);
    Optional<Requester> findByUsername(String username);
}
