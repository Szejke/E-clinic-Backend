package pl.medicalcenter.infrastructure.users.visits.event;

import org.springframework.context.ApplicationEvent;
import pl.medicalcenter.domain.Visit;

public class VisitReservationEvent extends ApplicationEvent {
    public VisitReservationEvent(Visit visit) {
        super(visit);
    }
}
