package pl.medicalcenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "Visit")
public class Visit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = Patient.class)
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = Doctor.class)
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;

    private BigDecimal price;

    private String status = VisitStatuses.NEW;

    @OneToOne(mappedBy = "visit", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Payment payment;

    private String receipt;

    @Min(15)
    @Max(60)
    private Integer duration;

    @Min(2018)
    private Integer year;

    @Min(1)
    @Max(12)
    private Integer month;

    @Min(1)
    @Max(31)
    private Integer day;

    @Min(0)
    @Max(23)
    private Integer hour;

    @Min(0)
    @Max(60)
    private Integer minutes;

    private String diagnosis;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return Objects.equals(id, visit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
