package pl.medicalcenter.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "Patient")
public class Patient {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name = "";

    private String surname = "";

    @Column(unique = true)
    private String email = "";

    private String password = "";

    private String pesel = "";

    @Size(min = 2, max = 35)
    private String address = "";

    @Size(min = 6, max = 6)
    @Pattern(regexp = "[0-9]{2}\\-[0-9]{3}")
    private String postalCode = "00-000";

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Visit> visits = new HashSet<>();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient user = (Patient) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
