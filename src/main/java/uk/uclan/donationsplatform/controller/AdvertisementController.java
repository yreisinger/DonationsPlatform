package uk.uclan.donationsplatform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.uclan.donationsplatform.beans.Advertisement;
import uk.uclan.donationsplatform.beans.Requester;
import uk.uclan.donationsplatform.repositories.AdvertisementRepository;
import uk.uclan.donationsplatform.repositories.RequesterRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdvertisementController {
    private final AdvertisementRepository advertisementRepository;

    private final RequesterRepository requesterRepository;

    @ModelAttribute
    public void setAttributes(Model model) {
        model.addAttribute("ads", advertisementRepository.findAll());
    }

    @ModelAttribute
    public Advertisement getAdvertisement() {
        return new Advertisement();
    }

    @GetMapping
    public String showHomePage(Model model)    {
        List<Advertisement> allAds = advertisementRepository.findAll();

        Collections.reverse(allAds);

        model.addAttribute("latestAds", allAds.stream().limit(3).toList());

        return "home";
    }

    @GetMapping("/ads")
    public String showAdsPage(@RequestParam(required = false) String searchTerm, Model model) {
        List<Advertisement> ads;

        if (searchTerm == null || searchTerm.isEmpty()) {
            ads = advertisementRepository.findAll();
        } else {
            ads = advertisementRepository.findAllByDescriptionContainingIgnoreCase(searchTerm);
        }

        model.addAttribute("ads", ads);
        model.addAttribute("searchTerm", searchTerm);

        return "adList";
    }

    @GetMapping("/ad/create")
    public String showAdCreatePage(Model model, Principal principal)    {
        model.addAttribute("currentRequester", requesterRepository.findByUsername(principal.getName()).get());

        return "adCreate";
    }

    @GetMapping("/ad/{id}")
    public String showAdDetails(Model model, @PathVariable("id") Integer id)   {
        if(advertisementRepository.findById(id).isPresent())  {
            model.addAttribute("currentAd", advertisementRepository.findById(id).get());
        }else {
            return "home";
        }

        return "redirect:/adDetails";
    }

    @GetMapping("/inventory")
    public String showInventory(Model model, Principal principal)   {
        model.addAttribute("inventory", advertisementRepository.findAllByRequester(requesterRepository.findByUsername(principal.getName()).get()));

        return "inventory";
    }

    @PostMapping("/api/ad/create")
    public String createAd(@ModelAttribute Advertisement advertisement, @RequestParam MultipartFile file, Principal principal, Model model) throws IOException {
        try {
            advertisement.setPicture(file.getBytes());
            advertisement.setRequester(requesterRepository.findByUsername(principal.getName()).get());

            advertisementRepository.save(advertisement);

            return "redirect:/inventory";
        } catch (Exception ex)   {
            return "adCreate";
        }
    }

    @GetMapping("/display/picture")
    public ResponseEntity<byte[]> displayPicture(@RequestParam("id") int id) throws SQLException {
        Advertisement advertisement = advertisementRepository.findById(id).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + advertisement.getWallet() + "\"")
                .body(advertisement.getPicture());
    }

    @GetMapping("/swap")
    public String showSwapPage()    {
        return "swap";
    }

    @GetMapping("/faq")
    public String showFAQPage() {
        return "faq";
    }
}
