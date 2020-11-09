package pl.medicalcenter.rest.visits.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.medicalcenter.domain.Visit;
import pl.medicalcenter.infrastructure.users.visits.doctor.DoctorVisitsJpaRepository;
import pl.medicalcenter.infrastructure.users.visits.doctor.DoctorVisitsService;
import pl.medicalcenter.infrastructure.users.visits.doctor.VisitNotFoundException;

import java.util.Set;

@CrossOrigin
@RestController
@PreAuthorize("hasAuthority('doctor')")
public class DoctorVisitsRest {

    private DoctorVisitsJpaRepository doctorVisitsJpaRepository;

    private DoctorVisitsService doctorVisitsService;

    public DoctorVisitsRest(DoctorVisitsJpaRepository repository, DoctorVisitsService doctorVisitsService) {
        this.doctorVisitsJpaRepository = repository;
        this.doctorVisitsService = doctorVisitsService;
    }

    @GetMapping(value = "/doctor/{id}/visits/{year}/{month}/{day}")
    public Set<Visit> getDoctorVisits(@PathVariable("id") Long id, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("day") Integer day) {
        return doctorVisitsJpaRepository.findAllByDoctorIdAndYearAndMonthAndDay(id, year, month, day);
    }

    @DeleteMapping(value = "/doctor/{doctorId}/visits/{visitId}")
    public ResponseEntity<HttpStatus> deleteVisit(@PathVariable("visitId") Long visitId, @PathVariable("doctorId") Long doctorId) {
        doctorVisitsService.tryDeleteVisit(visitId, doctorId);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping(value = "/doctor/{doctorId}/visits/{visitId}")
    public ResponseEntity<HttpStatus> updateVisit(@PathVariable("visitId") Long visitId, @PathVariable("doctorId") Long doctorId, @RequestBody UpdateVisitResource resource) {
        try {
            doctorVisitsService.tryUpdateVisit(visitId, doctorId, resource);
            return ResponseEntity.accepted().build();
        } catch (VisitNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/doctor/{id}/visits")
    public ResponseEntity addVisits(@PathVariable("id") Long id, @RequestBody AddVisitsResource resource) {
        return doctorVisitsService.tryAddVisits(id, resource);
    }
}
