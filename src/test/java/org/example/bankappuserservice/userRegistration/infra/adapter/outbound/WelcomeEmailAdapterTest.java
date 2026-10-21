package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WelcomeEmailAdapterTest {

    private final JavaMailSender javaMailSender =
            Mockito.mock(JavaMailSender.class);


    private final String from = "novabank@test.com";


    private final WelcomeEmailAdapter welcomeEmailAdapter = new WelcomeEmailAdapter(from, javaMailSender);

    @Test
    void sendWelcomeEmail_shouldSendCorrectMessage () {

        String name = "Test user";
        String email = "user@test.com";

        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        welcomeEmailAdapter.sendWelcomeEmail(email, name);

        Mockito.verify(javaMailSender).send(argumentCaptor.capture());

        SimpleMailMessage message = argumentCaptor.getValue();

        assertEquals("novabank@test.com" , message.getFrom());
        assertArrayEquals(new String[]{"user@test.com"}, message.getTo());
        assertEquals("Welcome to NovaBank" , message.getSubject());
        assertEquals("Hello " + name + ",\n\n" +
                "Welcome to NovaBank! Your registration has been received.\n\n" +
                "Best regards,\nNovaBank Team", message.getText());

    }

}
