package pl.medicalcenter.rest.visits.doctor;

import lombok.Data;

@Data
public class UpdateVisitResource {

    public final String receipt;
    public final String diagnosis;
}
