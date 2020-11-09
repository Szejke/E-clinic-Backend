package pl.medicalcenter.infrastructure.users.visits.event;

import org.springframework.context.ApplicationEvent;
import pl.medicalcenter.domain.Patient;
import pl.medicalcenter.domain.Visit;

import java.util.Optional;

public class VisitAbortEvent extends ApplicationEvent {
    public VisitAbortEvent(Patient patient) {
        super(patient);
    }
}
