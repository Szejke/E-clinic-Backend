package pl.medicalcenter.infrastructure.users.visits.doctor;

import org.springframework.data.repository.CrudRepository;
import pl.medicalcenter.domain.Visit;

import java.util.Optional;
import java.util.Set;

public interface DoctorVisitsJpaRepository extends CrudRepository<Visit, Long> {

    Set<Visit> findAllByDoctorIdAndYearAndMonthAndDay(Long userId, Integer year, Integer month, Integer day);

    Optional<Visit> findVisitByIdAndDoctorId(Long visitId, Long userId);

    Set<Visit> findAllByDoctorIdAndYearAndMonthAndDay(Long doctorId, int year, int month, int day);
}
