package uk.uclan.donationsplatform.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer adId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_id")
    @JsonBackReference
    private Requester requester;

    private String description;

    @Lob
    private byte[] picture;

    private String wallet;

    @Override
    public String toString() {
        return "" + adId;
    }
}
