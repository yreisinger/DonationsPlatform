package uk.uclan.donationsplatform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.uclan.donationsplatform.beans.Advertisement;
import uk.uclan.donationsplatform.repositories.AdvertisementRepository;

import java.util.List;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementRepository advertisementRepository;

    @GetMapping
    private ResponseEntity<List<Advertisement>> getAllAds() {
        return ResponseEntity.ok(advertisementRepository.findAll());
    }
}
