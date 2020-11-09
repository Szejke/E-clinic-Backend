package pl.medicalcenter.infrastructure.users.visits.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import pl.medicalcenter.domain.Visit;

@Lazy(false)
public class VisitPaidEvent extends ApplicationEvent {

    public VisitPaidEvent(Visit visit) {
        super(visit);
    }
}
