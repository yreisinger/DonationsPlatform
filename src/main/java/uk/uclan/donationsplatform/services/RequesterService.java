package uk.uclan.donationsplatform.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.uclan.donationsplatform.repositories.RequesterRepository;

@Service
@RequiredArgsConstructor
public class RequesterService implements UserDetailsService {

    private final RequesterRepository requesterRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return requesterRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Requester not valid"));
    }
}
