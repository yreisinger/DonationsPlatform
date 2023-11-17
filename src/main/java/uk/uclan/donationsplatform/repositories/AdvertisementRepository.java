package uk.uclan.donationsplatform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.uclan.donationsplatform.beans.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
}
