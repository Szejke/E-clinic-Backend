package pl.medicalcenter.rest.users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.medicalcenter.infrastructure.users.patient.PatientDataConverter;
import pl.medicalcenter.infrastructure.users.patient.PatientJpaRepository;
import pl.medicalcenter.infrastructure.users.patient.PatientNotFoundException;

@CrossOrigin
@RestController
@PreAuthorize("hasAuthority('patient')")
public class PatientRest {

    private PatientJpaRepository patientJpaRepository;

    public PatientRest(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    @GetMapping(value = "/patients/{id}")
    public ResponseEntity getPatientData(@PathVariable("id") Long patientId) {
        try {
            return ResponseEntity.ok(patientJpaRepository.findById(patientId).orElseThrow(PatientNotFoundException::new));
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(value = "/patients/{id}")
    public ResponseEntity updateData(@PathVariable("id") Long patientId, @RequestBody PatientDataResource patientDataResource) {
        try {
            return ResponseEntity.ok(PatientDataConverter.convert(patientId, patientDataResource, patientJpaRepository));
        } catch (PatientNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
