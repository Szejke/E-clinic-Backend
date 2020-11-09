package pl.medicalcenter.infrastructure.users.visits.event;

import org.springframework.context.ApplicationEvent;
import pl.medicalcenter.domain.Patient;

import java.math.BigDecimal;

public class ToPaidAfterPostponeVisitEvent extends ApplicationEvent {

    private Patient patient;
    private BigDecimal moneyDifference;

    public ToPaidAfterPostponeVisitEvent(Patient patient, BigDecimal moneyDifference) {
        super(patient);
        this.patient = patient;
        this.moneyDifference = moneyDifference;
    }
}
