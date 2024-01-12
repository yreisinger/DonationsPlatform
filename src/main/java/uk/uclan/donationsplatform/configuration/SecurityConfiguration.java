package uk.uclan.donationsplatform.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import uk.uclan.donationsplatform.repositories.RoleRepository;
import uk.uclan.donationsplatform.services.RequesterService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final RequesterService requesterService;
    private final RoleRepository roleRepository;

    @Bean
    public PasswordEncoder passwordEncoder()    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(login -> {
                    login.loginPage("/login");
                    login.permitAll();
                })
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/ad/create", "/inventory", "/api/ad/create").authenticated();
                    req.requestMatchers("/admin").hasAuthority("ADMIN");
                    req.requestMatchers("/auth/*", "/login", "/register").anonymous();
                    req.anyRequest().permitAll();
                })
                .userDetailsService(requesterService)
                .logout(logout -> {
                    logout.logoutSuccessUrl("/");
                })
                .build();
    }

}
