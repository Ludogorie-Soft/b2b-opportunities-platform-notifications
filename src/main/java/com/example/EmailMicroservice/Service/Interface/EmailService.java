package com.example.EmailMicroservice.Service.Interface;

import com.example.EmailMicroservice.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest emailRequest);
}
