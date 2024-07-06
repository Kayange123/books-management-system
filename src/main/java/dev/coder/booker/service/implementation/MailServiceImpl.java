package dev.coder.booker.service.implementation;

import dev.coder.booker.enumeration.EmailTemplate;
import dev.coder.booker.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    @Async
    public void sendMail(String to, String username, EmailTemplate emailTemplate, String confirmationUrl, String activationCode, String subject) throws MessagingException {
        String templateName;
        if(emailTemplate == null) {
            templateName = "confirm_email";
        }else {
            templateName = emailTemplate.getTemplateName();
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        Map<String, Object> properties = Map.of("username", username, "confirmationUrl", confirmationUrl, "activationCode", activationCode);
        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("kayangejr3@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);
        helper.setText(template, true);

        javaMailSender.send(mimeMessage);
    }
}
