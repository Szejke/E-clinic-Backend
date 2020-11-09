package pl.medicalcenter.infrastructure.users.patient;

import org.springframework.data.repository.CrudRepository;
import pl.medicalcenter.domain.Patient;

import java.util.Optional;

public interface PatientJpaRepository extends CrudRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findById(Long id);
}
