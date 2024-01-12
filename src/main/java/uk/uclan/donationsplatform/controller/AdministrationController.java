package uk.uclan.donationsplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.uclan.donationsplatform.beans.AdministrationDTO;
import uk.uclan.donationsplatform.beans.Requester;
import uk.uclan.donationsplatform.repositories.RequesterRepository;
import uk.uclan.donationsplatform.repositories.RoleRepository;

import java.beans.Encoder;
import java.security.Principal;
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

    @PostMapping("/auth/register")
    public String registerRequester(@ModelAttribute AdministrationDTO administrationDTO)   {
        try {
            Optional<Requester> optionalRequester = requesterRepository.findByUsernameContainingIgnoreCase(administrationDTO.getUsername());

            if(optionalRequester.isPresent())   {
                throw new Exception("Username already exists");
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            Requester requester = new Requester(administrationDTO.getUsername(), encoder.encode(administrationDTO.getPassword()));

            requester.getAuthorities().add(roleRepository.findRoleByAuthority("CREATOR").get());

            requesterRepository.save(requester);

            return "home";
        } catch (Exception e) {
            return "register";
        }
    }





}
