package pl.medicalcenter.infrastructure.users.visits.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.medicalcenter.domain.Visit;
import pl.medicalcenter.infrastructure.users.email.EmailService;
import pl.medicalcenter.infrastructure.users.email.Mail;
import pl.medicalcenter.infrastructure.users.visits.event.GiveBackMoneyAfterPostponeVisitEvent;

@Lazy(false)
@Component
public class GiveBackMoneyAfterPostponeVisitEventListener implements ApplicationListener<GiveBackMoneyAfterPostponeVisitEvent> {

    private EmailService emailService;

    public GiveBackMoneyAfterPostponeVisitEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(GiveBackMoneyAfterPostponeVisitEvent event) {
        Visit visit = event.getVisit();
        emailService.sendSimpleMessage(Mail.builder()
                .from("emedicalcliniccenter@gmail.com")
                .to(event.getPatient().getEmail())
                .subject("Przełożenie wizyty - zwrot pieniędzy")
                .content("Wizyta w dniu: " + getFormattedDate(visit) + " " + getFormattedTime(visit) + " Do lekarza: " + visit.getDoctor().getName() + " " + visit.getDoctor().getSurname() + " : " + visit.getDoctor().getSpecialization() + " została przełożona. Kwota zostanie zwrócona na twoje konto. Dokonaj nowej płatności: <a href='http://localhost:4200/patient/dashboard'>Zapłać</a>")
                .build());
    }

    private String getFormattedDate(Visit visit) {
        return visit.getYear() + "-" + (visit.getMonth() < 10 ? "0" : "") + visit.getMonth() + "-" + (visit.getDay() < 10 ? "0" : "" + visit.getDay());
    }

    private String getFormattedTime(Visit visit) {
        return visit.getHour() + ":" + (visit.getMinutes() < 10 ? "0" : "") + visit.getMinutes();
    }
}
