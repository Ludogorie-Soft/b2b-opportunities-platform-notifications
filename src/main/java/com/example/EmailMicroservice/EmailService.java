package com.example.EmailMicroservice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${aws.ses.sender}")
    private String fromMail;

    private final SesClient sesClient;

    public void sendEmail(EmailRequest emailRequest) {
        try {
            Destination destination = Destination.builder()
                    .toAddresses(emailRequest.getReceiver())
                    .build();

            Content subject = Content.builder()
                    .data(emailRequest.getSubject())
                    .build();

            Content bodyContent = Content.builder()
                    .data(emailRequest.getContent())
                    .build();

            Body body = Body.builder()
                    .html(bodyContent)
                    .build();

            Message message = Message.builder()
                    .subject(subject)
                    .body(body)
                    .build();

            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromMail)
                    .destination(destination)
                    .message(message)
                    .build();

            sesClient.sendEmail(request);
        } catch (SesException e) {
            throw new ServerErrorException("Failed to send email via AWS SES: " + e.awsErrorDetails().errorMessage());
        }
    }
}
