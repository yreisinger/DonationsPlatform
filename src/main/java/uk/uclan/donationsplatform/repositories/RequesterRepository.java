package uk.uclan.donationsplatform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.uclan.donationsplatform.beans.Requester;

public interface RequesterRepository extends JpaRepository<Requester, Integer> {
}
