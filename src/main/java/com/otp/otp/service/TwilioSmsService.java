package com.otp.otp.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    public void sendOtpSms(String toPhoneNumber, String otp) {
        Twilio.init(accountSid, authToken);

        PhoneNumber to = new PhoneNumber(toPhoneNumber);
        PhoneNumber from = new PhoneNumber(fromPhoneNumber);

        String messageBody = "Your OTP code is: " + otp;

        Message message = Message.creator(to, from, messageBody).create();

        System.out.println("OTP sent with SID: " + message.getSid());
    }
}

