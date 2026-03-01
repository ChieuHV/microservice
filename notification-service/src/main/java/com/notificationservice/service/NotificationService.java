package com.notificationservice.service;

import jakarta.mail.MessagingException;

public interface NotificationService {
    void sendMail(String to, String subject, String htmlContent) throws MessagingException;
}
