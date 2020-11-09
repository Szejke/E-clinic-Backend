package pl.medicalcenter.infrastructure.users.visits.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.medicalcenter.domain.Visit;
import pl.medicalcenter.infrastructure.users.email.EmailService;
import pl.medicalcenter.infrastructure.users.email.Mail;
import pl.medicalcenter.infrastructure.users.visits.event.VisitReservationEvent;

@Lazy(false)
@Component
public class VisitReservationEventListener implements ApplicationListener<VisitReservationEvent> {

    private EmailService emailService;

    public VisitReservationEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(VisitReservationEvent event) {
        Visit visit = (Visit)event.getSource();
        emailService.sendSimpleMessage(Mail.builder()
                .from("emedicalcliniccenter@gmail.com")
                .to(visit.getPatient().getEmail())
                .subject("Potwierdzenie rezerwacji")
                .content("Wizyta w dniu: " + getFormattedDate(visit) + " " + getFormattedTime(visit) + " Do lekarza: " + visit.getDoctor().getName() + " " + visit.getDoctor().getSurname() + " : " + visit.getDoctor().getSpecialization() + " zostałą zarezerwowana. Dokonaj płatności: <a href='http://localhost:4200/patient/dashboard'>Zapłać</a>")
                .build());
    }

    private String getFormattedDate(Visit visit) {
        return visit.getYear() + "-" + (visit.getMonth() < 10 ? "0" : "") + visit.getMonth() + "-" + (visit.getDay() < 10 ? "0" : "" + visit.getDay());
    }

    private String getFormattedTime(Visit visit) {
        return visit.getHour() + ":" + (visit.getMinutes() < 10 ? "0" : "") + visit.getMinutes();
    }
}
