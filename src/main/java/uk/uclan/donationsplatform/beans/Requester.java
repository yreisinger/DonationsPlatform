package uk.uclan.donationsplatform.beans;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Requester implements UserDetails {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "req_id")
    private Integer reqId;

    @Setter
    private String username;

    @Setter
    private String password;

    @Setter
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Role> authorities = new HashSet<Role>();

    @Getter
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Advertisement> advertisements;

    @Override
    public Set<Role> getAuthorities() {
        return this.authorities;
    }

    public Requester(Integer reqId, String username, String password, Set<Role> authorities) {
        this.reqId = reqId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public Requester(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Function to verify the user with the document
    @Override
    public boolean isEnabled() {
        return true;
    }

}
