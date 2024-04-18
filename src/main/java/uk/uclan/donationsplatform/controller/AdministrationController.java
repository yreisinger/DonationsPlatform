package uk.uclan.donationsplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.uclan.donationsplatform.beans.AdministrationDTO;
import uk.uclan.donationsplatform.beans.Advertisement;
import uk.uclan.donationsplatform.beans.Requester;
import uk.uclan.donationsplatform.repositories.RequesterRepository;
import uk.uclan.donationsplatform.repositories.RoleRepository;

import java.beans.Encoder;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
@SessionAttributes({"loggedUser"})
public class AdministrationController {

    private final RequesterRepository requesterRepository;

    private final RoleRepository roleRepository;

    @ModelAttribute
    public AdministrationDTO getAdministrationDTO() {
        return new AdministrationDTO();
    }

    @GetMapping("/register")
    public String showRegisterPage()    {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage()   {
        return "login";
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model)  {
        model.addAttribute("requesters", requesterRepository.findAll());

        return "admin";
    }

    @GetMapping("/verify")
    public String showVerifyPage(Model model, Principal principal)  {
        model.addAttribute("currentRequester", requesterRepository.findByUsername(principal.getName()).get());

        return "verify";
    }

    @PostMapping("/auth/register")
    public String registerRequester(@ModelAttribute AdministrationDTO administrationDTO, Model model)   {
        Optional<Requester> optionalRequester = requesterRepository.findByUsernameContainingIgnoreCase(administrationDTO.getUsername());

        if(optionalRequester.isEmpty())   {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            Requester requester = new Requester(administrationDTO.getUsername(), encoder.encode(administrationDTO.getPassword()));

            requester.getAuthorities().add(roleRepository.findRoleByAuthority("CREATOR").get());

            requesterRepository.save(requester);

            return "redirect:/";
        }else {
            model.addAttribute("showError", true);

            return "register";
        }
    }

    @PostMapping("/auth/verify")
    public String verifyRequester(@RequestParam MultipartFile file, Principal principal) {
        try{
            Requester requester = requesterRepository.findByUsername(principal.getName()).get();

            requester.setVerifyDocument(file.getBytes());

            requesterRepository.save(requester);

            return "redirect:/";
        } catch (Exception ex)   {
            return "verify";
        }
    }

    @GetMapping("/display/verifyDocument")
    public ResponseEntity<byte[]> displayPicture(@RequestParam("id") int id) throws SQLException {
        Requester requester = requesterRepository.findById(id).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + requester.getReqId() + "\"")
                .body(requester.getVerifyDocument());
    }

    @PostMapping("/admin/verify")
    public String verifyRequesterAsAdmin(@RequestParam("id") int id)  {
        try{
            Requester requester = requesterRepository.findById(id).get();

            requester.setVerified(true);

            requesterRepository.save(requester);

            return "redirect:/admin";
        }catch (Exception ex)   {
            return "admin";
        }
    }

}
