package com.eexchange.backend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${RESEND_API_KEY:}")
    private String resendApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOtpEmail(String toEmail, String otpCode) {
//        if (resendApiKey == null || resendApiKey.isEmpty()) {
//            System.out.println("DEBUG: OTP for " + toEmail + " is " + otpCode);
//            return;
//        }

        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("from", "EExchange <onboarding@resend.dev>");
        body.put("to", toEmail);
        body.put("subject", "Your OTP for EExchange Registration");
        body.put("html", "<strong>Your OTP for registration is: " + otpCode + "</strong><br>This OTP is valid for 5 minutes.");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("OTP Email sent successfully via Resend to " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email via Resend: " + e.getMessage());
            throw new RuntimeException("Email service temporarily unavailable");
        }
    }
}
