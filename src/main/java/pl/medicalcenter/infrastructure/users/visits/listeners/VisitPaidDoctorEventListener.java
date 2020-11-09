package pl.medicalcenter.infrastructure.users.visits.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.medicalcenter.domain.Visit;
import pl.medicalcenter.infrastructure.users.email.EmailService;
import pl.medicalcenter.infrastructure.users.email.Mail;
import pl.medicalcenter.infrastructure.users.visits.event.VisitPaidEvent;

@Lazy(false)
@Component
public class VisitPaidDoctorEventListener implements ApplicationListener<VisitPaidEvent> {

    private EmailService emailService;

    public VisitPaidDoctorEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(VisitPaidEvent event) {
        Visit visit = (Visit)event.getSource();
        emailService.sendSimpleMessage(Mail.builder()
                .from("emedicalcliniccenter@gmail.com")
                .to(visit.getPatient().getEmail())
                .subject("Potwierdzenie zapłaty")
                .content("Płatność za wizytę w dniu: " + getFormattedDate(visit) + " " + getFormattedTime(visit) + " Przez pacjenta: " + visit.getPatient().getName() + " " + visit.getPatient().getSurname())
                .build());
    }

    private String getFormattedDate(Visit visit) {
        return visit.getYear() + "-" + (visit.getMonth() < 10 ? "0" : "") + visit.getMonth() + "-" + (visit.getDay() < 10 ? "0" : "" + visit.getDay());
    }

    private String getFormattedTime(Visit visit) {
        return visit.getHour() + ":" + (visit.getMinutes() < 10 ? "0" : "") + visit.getMinutes();
    }
}
