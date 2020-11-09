package pl.medicalcenter.infrastructure.users.doctor;

import org.springframework.data.repository.CrudRepository;
import pl.medicalcenter.domain.Doctor;

import java.util.Optional;

public interface DoctorJpaRepository extends CrudRepository<Doctor, Long> {
    Optional<Doctor> findById(Long id);
    Optional<Doctor> findOneByEmail(String email);
}
