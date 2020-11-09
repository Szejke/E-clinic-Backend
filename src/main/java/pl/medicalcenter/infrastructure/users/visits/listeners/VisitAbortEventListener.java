package pl.medicalcenter.infrastructure.users.visits.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.medicalcenter.domain.Patient;
import pl.medicalcenter.infrastructure.users.email.EmailService;
import pl.medicalcenter.infrastructure.users.email.Mail;
import pl.medicalcenter.infrastructure.users.visits.event.VisitAbortEvent;

@Lazy(false)
@Component
public class VisitAbortEventListener implements ApplicationListener<VisitAbortEvent> {

    private EmailService emailService;

    public VisitAbortEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(VisitAbortEvent visitAbortEvent) {
        Patient patient = (Patient)visitAbortEvent.getSource();
        emailService.sendSimpleMessage(Mail.builder()
                .from("emedicalcliniccenter@gmail.com")
                .to(patient.getEmail())
                .subject("Odwołanie wizyty")
                .content("Wizyta zostałą odwołana. Pieniądze zostaną zwrócone na konto.")
                .build());
    }
}
