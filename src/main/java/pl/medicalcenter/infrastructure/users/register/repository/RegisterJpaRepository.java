package pl.medicalcenter.infrastructure.users.register.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.medicalcenter.domain.Patient;

import java.util.Optional;

@Repository
public interface RegisterJpaRepository extends CrudRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

}
