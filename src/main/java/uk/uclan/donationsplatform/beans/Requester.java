package uk.uclan.donationsplatform.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.sql.ast.tree.predicate.BooleanExpressionPredicate;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Requester {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    private String email;

    private String password;

    private String verificationDocument;

    @Column(columnDefinition = "boolean default false")
    private Boolean isVerified;

    @Column(columnDefinition = "boolean default false")
    private Boolean isAdmin;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    private List<Advertisement> advertisements;
}
