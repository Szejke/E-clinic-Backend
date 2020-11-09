package pl.medicalcenter.infrastructure.users.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final static String MOCK_EMAIL = "emedicalcliniccenter@gmail.com";

    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(final Mail mail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
//        message.setTo(mail.getTo());
        message.setTo(MOCK_EMAIL);
        message.setFrom(MOCK_EMAIL);
//        message.setFrom(mail.getFrom());

        emailSender.send(message);
    }
}
