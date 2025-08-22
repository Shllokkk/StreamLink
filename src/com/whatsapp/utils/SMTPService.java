/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.whatsapp.utils;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Owner
 */
public class SMTPService {
    
    public static void sendVerificationMail(String email, String code) {
        // Sender email credentials
        final String senderEmail = "322shllok0064@dbit.in";
        final String senderPassword = "khlb whrg kymn bvjm"; // Use App Password for Gmail instead of actual account password
        
        // Recipient email
        String recipientEmail = email;
        
        // Set SMTP Server Properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP server
        properties.put("mail.smtp.port", "587"); // Port for TLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS
        
        // Create session
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Verification Code");
            message.setText("Your verification code is " + code);

            // Send email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public static String randomCodeGenerator() {
        Random random = new Random();
        int randomValue = 100000 + random.nextInt(900000);

        return String.valueOf(randomValue);
    }
}
