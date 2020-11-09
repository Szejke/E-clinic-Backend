package pl.medicalcenter.infrastructure.users.visits.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.medicalcenter.infrastructure.users.email.EmailService;
import pl.medicalcenter.infrastructure.users.email.Mail;
import pl.medicalcenter.infrastructure.users.visits.event.GiveBackMoneyAfterPostponeVisitEvent;

@Lazy(false)
@Component
public class EmailGiveBackMoneyAfterPostponeVisitEventListener implements ApplicationListener<GiveBackMoneyAfterPostponeVisitEvent> {

    private EmailService emailService;

    public EmailGiveBackMoneyAfterPostponeVisitEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(GiveBackMoneyAfterPostponeVisitEvent event) {
        emailService.sendSimpleMessage(Mail.builder()
                .from("emedicalcliniccenter@gmail.com")
                .to(event.getPatient().getEmail())
                .subject("Przełożenie wizyty - zwrot pieniędzy")
                .content("Kwota: " + event.getOddMoney() + " zostanie zwrócona na konto PayPal. Wykonaj płatność za nowo wybraną wizytę.")
                .build());
    }
}
