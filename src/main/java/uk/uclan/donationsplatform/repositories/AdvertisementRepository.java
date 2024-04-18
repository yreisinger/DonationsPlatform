package uk.uclan.donationsplatform.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.uclan.donationsplatform.beans.Advertisement;
import uk.uclan.donationsplatform.beans.Requester;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    List<Advertisement> findAllByRequester(Requester requester);
    List<Advertisement> findAllByDescriptionContainingIgnoreCase(String searchTerm);
}
