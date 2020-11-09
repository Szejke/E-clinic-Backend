package pl.medicalcenter.rest.visits.patient;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.medicalcenter.domain.Visit;
import pl.medicalcenter.infrastructure.users.visits.patient.PatientVisitsJpaRepository;
import pl.medicalcenter.infrastructure.users.visits.patient.PatientVisitsService;

import java.time.LocalDate;
import java.util.Set;

@CrossOrigin
@RestController
@PreAuthorize("hasAuthority('patient')")
public class PatientVisitsRest {

    private PatientVisitsJpaRepository patientVisitsJpaRepository;

    private PatientVisitsService patientVisitsService;

    public PatientVisitsRest(PatientVisitsJpaRepository patientVisitsJpaRepository, PatientVisitsService patientVisitsService) {
        this.patientVisitsJpaRepository = patientVisitsJpaRepository;
        this.patientVisitsService = patientVisitsService;
    }

    @GetMapping(value = "/patient/{id}/visits")
    public Set<Visit> getPatientVisits(@PathVariable("id") Long id) {
        return patientVisitsJpaRepository.findAllByPatientId(id);
    }

    @GetMapping(value = "/patient/visits/allowed")
    public Set<Visit> getAllowedVisits() {
        LocalDate now = LocalDate.now();
        return patientVisitsJpaRepository.findAllowedVisits(now.getYear(), now.getMonth().getValue() - 1, now.getDayOfMonth());
    }

    @PostMapping(value = "/patient/{patientId}/visits/{visitId}/reservation")
    public ResponseEntity tryReserveVisit(@PathVariable("patientId") Long patientId, @PathVariable("visitId") Long visitId) {
        return patientVisitsService.reserveVisit(visitId, patientId);
    }

    @PatchMapping(value = "/patient/{patientId}/visits/{visitId}/reservation/abort")
    public ResponseEntity tryAbortVisit(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId) {
        return patientVisitsService.abortVisit(visitId, patientId);
    }

    @GetMapping(value = "/patient/{patientId}/visits/{visitId}/postpone")
    public ResponseEntity visitsForPostpone(@PathVariable("patientId") Long patientId, @PathVariable("visitId") Long visitId) {
        return patientVisitsService.findVisitsForPostpone(patientId, visitId);
    }

    @GetMapping(value = "/patient/{patientId}/visits/{visitId}/postpone/{year}/{month}/{day}")
    public ResponseEntity visitsForPostponeByDay(@PathVariable("patientId") Long patientId, @PathVariable("visitId") Long visitId, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("day") Integer day) {
        return patientVisitsService.findVisitsForPostponeByDate(patientId, visitId, year, month, day);
    }

    @PatchMapping(value = "/patient/{patientId}/visits/{visitId}/postpone/{otherVisitId}")
    public ResponseEntity tryPostponeVisit(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId, @PathVariable("otherVisitId") Long otherVisitId) {
        return patientVisitsService.postponeVisit(visitId, patientId, otherVisitId);
    }

    @PatchMapping(value = "/patient/{patientId}/visits/{visitId}/payment")
    public ResponseEntity tryPay(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId) {
        return patientVisitsService.pay(visitId, patientId);
    }


}
