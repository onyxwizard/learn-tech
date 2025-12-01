package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.impl;

// di/impl/SmtpEmailService.java

import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.service.EmailService;

public class SmtpEmailService implements EmailService {
    @Override
    public void send(String to, String subject, String body) {
        System.out.println("[EMAIL] To: " + to + " | " + subject + " | " + body);
    }
}