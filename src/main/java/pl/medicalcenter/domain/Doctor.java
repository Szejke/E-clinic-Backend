package pl.medicalcenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "Doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name = "";

    private String surname = "";

    private String password = "";

    @Column(unique = true)
    private String email = "";

    private String specialization = "";

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Visit> visits = new HashSet<>();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor user = (Doctor) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
