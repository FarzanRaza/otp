package com.otp.otp.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;



@RestController
@RequestMapping("/api/otp")
class OtpController {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    private final Map<String, String> otpStore = new HashMap<>();

    @PostMapping("/send")
    public String sendOtp(@RequestParam String phoneNumber) {
        // Generate a random 6-digit OTP
        String otp = generateRandomOtp(6);

        // Store the OTP temporarily (You should use a database for this)
        otpStore.put(phoneNumber, otp);

        // Send the OTP via Twilio
        Twilio.init(accountSid, authToken);
        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(fromPhoneNumber);
        String messageBody = "Your OTP code is: " + otp;
        Message message = Message.creator(to, from, messageBody).create();

        return "OTP sent to " + phoneNumber;
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        // Retrieve the stored OTP for the given phone number
        String storedOtp = otpStore.get(phoneNumber);

        if (storedOtp != null && storedOtp.equals(otp)) {
            // OTP is valid
            otpStore.remove(phoneNumber); // Remove the OTP from storage after successful verification
            return "OTP verification successful!";
        } else {
            // OTP is invalid
            return "Invalid OTP.";
        }
    }

    // Helper method to generate a random OTP of a given length
    private String generateRandomOtp(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}

