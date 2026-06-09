package br.com.instituto.teresa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final String recipient;
    private final String fromAddress;

    public EmailService(
            ObjectProvider<JavaMailSender> mailSenderProvider,
            @Value("${mail.notification.recipient:}") String recipient,
            @Value("${spring.mail.username:}") String fromAddress) {
        this.mailSenderProvider = mailSenderProvider;
        this.recipient = recipient;
        this.fromAddress = fromAddress;
    }

    public void sendVolunteerNotification(String name, String email, String phone, String age, String motivation) {
        JavaMailSender sender = mailSenderProvider.getIfAvailable();
        if (sender == null || recipient.isBlank()) {
            log.debug("SMTP não configurado ou destinatário ausente — notificação de candidatura não enviada.");
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(recipient);
            message.setSubject("Nova candidatura de voluntário — " + name);
            message.setText(String.format(
                "Nova candidatura recebida no site do Instituto Tereza de Benguela.\n\n" +
                "Nome: %s\nE-mail: %s\nTelefone: %s\nIdade: %s\n\nMotivação:\n%s",
                name, email, phone != null ? phone : "—", age != null ? age : "—", motivation
            ));
            sender.send(message);
            log.info("Notificação de candidatura enviada para {}", recipient);
        } catch (Exception e) {
            log.warn("Falha ao enviar e-mail de candidatura: {}", e.getMessage());
        }
    }
}
