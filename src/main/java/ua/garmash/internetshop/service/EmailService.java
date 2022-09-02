package ua.garmash.internetshop.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
