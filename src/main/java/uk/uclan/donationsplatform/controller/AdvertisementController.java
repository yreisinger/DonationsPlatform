package uk.uclan.donationsplatform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    private ResponseEntity<Advertisement> getAdDetails(@PathVariable("id") Integer id)   {
        if(advertisementRepository.findById(id).isPresent())  {
            return ResponseEntity.ok(advertisementRepository.findById(id).get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
