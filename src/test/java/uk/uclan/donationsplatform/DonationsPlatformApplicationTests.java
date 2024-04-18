package uk.uclan.donationsplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.multipart.MultipartFile;
import uk.uclan.donationsplatform.beans.AdministrationDTO;
import uk.uclan.donationsplatform.beans.Advertisement;
import uk.uclan.donationsplatform.beans.Requester;
import uk.uclan.donationsplatform.beans.Role;
import uk.uclan.donationsplatform.controller.AdministrationController;
import uk.uclan.donationsplatform.repositories.AdvertisementRepository;
import uk.uclan.donationsplatform.repositories.RequesterRepository;
import uk.uclan.donationsplatform.repositories.RoleRepository;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdministrationController.class)
@AutoConfigureMockMvc(addFilters = false)
class DonationsPlatformApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequesterRepository requesterRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    void testRegisterRequester() throws Exception {
        when(requesterRepository.findByUsernameContainingIgnoreCase("username")).thenReturn(Optional.empty());

        Role creatorRole = new Role();
        creatorRole.setAuthority("CREATOR");

        when(roleRepository.findRoleByAuthority("CREATOR")).thenReturn(Optional.of(creatorRole));

        AdministrationDTO administrationDTO = new AdministrationDTO();
        administrationDTO.setUsername("username");
        administrationDTO.setPassword("password");

        mockMvc.perform(post("/auth/register")
                        .flashAttr("administrationDTO", administrationDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        ArgumentCaptor<Requester> requesterCaptor = ArgumentCaptor.forClass(Requester.class);
        verify(requesterRepository, times(1)).save(requesterCaptor.capture());

        Requester savedRequester = requesterCaptor.getValue();
        assertEquals("username", savedRequester.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("password", savedRequester.getPassword()));
        assertTrue(savedRequester.getAuthorities().contains(creatorRole));
    }

    @Test
    void testRegisterRequesterWithExistingUsername() throws Exception {
        Requester existingRequester = new Requester();
        existingRequester.setUsername("username");
        existingRequester.setPassword("existingPassword");

        when(requesterRepository.findByUsernameContainingIgnoreCase("username")).thenReturn(Optional.of(existingRequester));

        AdministrationDTO administrationDTO = new AdministrationDTO();
        administrationDTO.setUsername("username");
        administrationDTO.setPassword("password");

        mockMvc.perform(post("/auth/register")
                        .flashAttr("administrationDTO", administrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("showError", true));

        verify(requesterRepository, never()).save(any(Requester.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testVerifyRequesterAsAdmin() throws Exception {
        Requester requester = new Requester();
        requester.setReqId(1);
        requester.setUsername("username");
        requester.setPassword("password");

        when(requesterRepository.findById(1)).thenReturn(Optional.of(requester));

        mockMvc.perform(post("/admin/verify")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection());

        assertTrue(requester.isVerified());
        verify(requesterRepository, times(1)).save(requester);
    }

    @Test
    @WithMockUser(username = "username", authorities = "CREATOR")
    void testVerifyRequester() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test document".getBytes());

        Requester requester = new Requester();
        requester.setUsername("username");
        requester.setPassword("password");

        when(requesterRepository.findByUsername("username")).thenReturn(Optional.of(requester));

        Principal principal = mock(Principal.class);

        when(principal.getName()).thenReturn("username");

        mockMvc.perform(get("/verify")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentRequester"));

        mockMvc.perform(multipart("/auth/verify")
                        .file(file)
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(requesterRepository, times(1)).save(any(Requester.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testSpringSecurityAuthority() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is2xxSuccessful());
    }
}
