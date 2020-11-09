package pl.medicalcenter.infrastructure.users.visits.patient;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.medicalcenter.domain.Visit;

import java.util.Optional;
import java.util.Set;

public interface PatientVisitsJpaRepository extends CrudRepository<Visit, Long> {
    Set<Visit> findAllByPatientId(Long userId);
    Optional<Visit> findVisitByIdAndPatientIsNull(Long visitId);

    @Query(value = "SELECT v FROM Visit v LEFT JOIN FETCH v.doctor WHERE v.patient.id = :patientId AND v.id = :visitId")
    Optional<Visit> findVisitByIdAndPatientId(@Param("visitId") Long visitId, @Param("patientId") Long patientId);

    @Query(value = "SELECT v FROM Visit v LEFT JOIN FETCH v.doctor WHERE v.patient is null and v.doctor is not null and status IN ('NOWA', 'PRZELOZONA', 'ODWOŁANA PRZEZ DOKTORA', 'ODWOŁANA PRZEZ PACJENTA') AND v.year >= :year and v.month >= :month AND day >= :day")
    Set<Visit> findAllowedVisits(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query(value = "SELECT v FROM Visit v LEFT JOIN FETCH v.doctor WHERE v.patient is null and v.doctor.id = :doctorId and status IN ('NOWA', 'PRZELOZONA', 'ODWOŁANA PRZEZ DOKTORA', 'ODWOŁANA PRZEZ PACJENTA') AND v.year >= :year and v.month >= :month AND day >= :day AND v.id <> :visitId")
    Set<Visit> findVisitsToPostpone(@Param("year") int year, @Param("month") int month, @Param("day") int day, @Param("doctorId") Long doctorId, @Param("visitId") Long visitId);

    @Query(value = "SELECT v FROM Visit v LEFT JOIN FETCH v.doctor WHERE v.patient is null and v.doctor.id = :doctorId and status IN ('NOWA', 'PRZELOZONA', 'ODWOŁANA PRZEZ DOKTORA', 'ODWOŁANA PRZEZ PACJENTA') AND v.year = :year and v.month = :month AND day = :day AND v.id <> :visitId")
    Set<Visit> findVisitsToPostponeByDay(@Param("year") int year, @Param("month") int month, @Param("day") int day, @Param("doctorId") Long doctorId,@Param("visitId") Long visitId);
}
