package com.sony.core.services.impl;

import com.sony.core.services.EmailService;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EmailService.class)
public class EmailServiceImpl implements EmailService {

    @Reference
    private MessageGatewayService messageGatewayService;
    private static final Logger LOG =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendEmail(String to, String subject, String body) {

        try {

            MessageGateway<HtmlEmail> gateway =
                    messageGatewayService.getGateway(HtmlEmail.class);

            if (gateway == null) {
                throw new RuntimeException("Message Gateway is null");
            }

            HtmlEmail email = new HtmlEmail();

            email.addTo(to);
            email.setSubject(subject);
            email.setHtmlMsg(body);

            gateway.send(email);
            LOG.info("Sending mail to {}", to);

        } catch (EmailException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}