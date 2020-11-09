package pl.medicalcenter.infrastructure.users.visits.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.medicalcenter.domain.Patient;
import pl.medicalcenter.domain.Visit;

import java.math.BigDecimal;

@Getter
public class GiveBackMoneyAfterPostponeVisitEvent extends ApplicationEvent {

    private Patient patient;
    private BigDecimal oddMoney;
    private Visit visit;

    public GiveBackMoneyAfterPostponeVisitEvent(Patient patient, BigDecimal oddMoney, Visit visit) {
        super(patient);
        this.patient = patient;
        this.oddMoney = oddMoney;
        this.visit = visit;
    }
}
