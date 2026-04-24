package com.eexchange.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    public void sendOtpEmail(String toEmail,String otpCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for EExchange Registration");
        message.setText("Yout OTP for registration is :" + otpCode + "\nThis OTP is valid for 5 minutes");
        javaMailSender.send(message);
    }
}
