package dev.coder.booker.service;

import dev.coder.booker.enumeration.EmailTemplate;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendMail(String to, String username, EmailTemplate emailTemplate, String confirmationUrl,
                  String activationCode, String subject) throws MessagingException;
}
