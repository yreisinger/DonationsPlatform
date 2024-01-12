package uk.uclan.donationsplatform;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.uclan.donationsplatform.beans.Role;
import uk.uclan.donationsplatform.repositories.RequesterRepository;
import uk.uclan.donationsplatform.repositories.RoleRepository;

@SpringBootApplication
public class DonationsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonationsPlatformApplication.class, args);
    }

}
