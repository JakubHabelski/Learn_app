package com.learn.app.Config;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MyMailSenderService {




   private final JavaMailSender javaMailSender;

    public MyMailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendEmail(String to, String subject, String text) {
       SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
         simpleMailMessage.setTo(to);
         simpleMailMessage.setSubject(subject);
         simpleMailMessage.setText(text);
         javaMailSender.send(simpleMailMessage);
    }

}
