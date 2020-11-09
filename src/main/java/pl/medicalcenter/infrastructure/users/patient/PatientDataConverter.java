package pl.medicalcenter.infrastructure.users.patient;

import pl.medicalcenter.domain.Patient;
import pl.medicalcenter.rest.users.PatientDataResource;

public class PatientDataConverter {

    public static Patient convert(Long id, PatientDataResource resource, PatientJpaRepository repository) throws PatientNotFoundException {
        return repository.findById(id)
                .map(patient -> {
                    patient.setName(resource.getName());
                    patient.setSurname(resource.getSurname());
                    patient.setPesel(resource.getPesel());
                    patient.setAddress(resource.getAddress());
                    patient.setPostalCode(resource.getPostalCode());
                    return patient;
                })
                .map(repository::save)
                .orElseThrow(PatientNotFoundException::new);
    }
}
