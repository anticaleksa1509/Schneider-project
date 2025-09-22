package raf.rs.java_mail.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @JmsListener(destination = "${destination.loginUser}", concurrency = "5-10")
    public void confirmLoginEmail(String message){

        try {
            String msgArray[] = message.split(" ");
            Long id_user = Long.valueOf(msgArray[0]);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

            mimeMessageHelper.setFrom("anticaleksa97@gmail.com");
            mimeMessageHelper.setTo("anticaleksa97@gmail.com");
            mimeMessageHelper.setSubject("Schneider Electric Internship application" + id_user);
            mimeMessageHelper.setText("User " + msgArray[1] + " "
                    + msgArray[2] + " has successfully logged in.");

            mimeMessageHelper.addAttachment("SchneiderImg (2).png",
                    new File("C:\\Users\\ma07271\\Downloads\\SchneiderImg (2).png"));

            javaMailSender.send(mimeMessage);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @JmsListener(destination = "${destination.registerUser}",concurrency = "5-10")
    public void confirmRegistrationEmail(String message){

        try {

            String[] msqArray = message.split(" ");

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

            mimeMessageHelper.setFrom("anticaleksa97@gmail.com");
            mimeMessageHelper.setTo("anticaleksa97@gmail.com");
            mimeMessageHelper.setSubject("Schneider Electric Internship application");
            mimeMessageHelper.setText("Successfully registered user " +
                    msqArray[1] + " with ID " + msqArray[0]);

            mimeMessageHelper.addAttachment("SchneiderImg (2).png",
                    new File("C:\\Users\\ma07271\\Downloads\\SchneiderImg (2).png"));
            // napraviti relativnu putanju!!!!!

            javaMailSender.send(mimeMessage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
