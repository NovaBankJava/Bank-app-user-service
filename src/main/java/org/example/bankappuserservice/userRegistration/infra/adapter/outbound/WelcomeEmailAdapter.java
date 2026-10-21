package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.example.bankappuserservice.userRegistration.domain.ports.out.AccountConfirmationEmailPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class WelcomeEmailAdapter implements AccountConfirmationEmailPort {

    private final JavaMailSender javaMailSender;
    private final String from;

    public WelcomeEmailAdapter(@Value("${app.mail.from}") String from, JavaMailSender javaMailSender) {
        this.from = from;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendWelcomeEmail (String email, String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Welcome to NovaBank");
        message.setText(
                "Hello " + name + ",\n\n" +
                        "Welcome to NovaBank! Your registration has been received.\n\n" +
                        "Best regards,\nNovaBank Team"
        );
        javaMailSender.send(message);

    }

}
