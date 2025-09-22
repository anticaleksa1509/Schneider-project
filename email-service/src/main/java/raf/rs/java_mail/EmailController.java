package raf.rs.java_mail;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;


    @RequestMapping(value = "/sendEmail")
    public String sendEmail(){

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("anticaleksa97@gmail.com");
            mailMessage.setTo("anticaleksa97@gmail.com");
            mailMessage.setSubject("Simple text email");
            mailMessage.setText("This is simple email body for my first email");

            javaMailSender.send(mailMessage);
            return "Success!";
        }catch (Exception e){
            return e.getMessage();
        }

    }

    @RequestMapping(value = "/sendEmailWithAttachment")
    public String sendEmailWithAttachment(){

        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

            mimeMessageHelper.setFrom("anticaleksa97@gmail.com");
            mimeMessageHelper.setTo("anticaleksa97@gmail.com");
            mimeMessageHelper.setSubject("Java Email with attachment");
            mimeMessageHelper.setText("Please find the attached document below");

            mimeMessageHelper.addAttachment("Rstudio2.PNG",
                    new File("C:\\Users\\ma07271\\Pictures\\Rstudio2.PNG"));

            mimeMessageHelper.addAttachment("CV_AnticAleksa.pdf",
                    new File("C:\\Users\\alekaa\\CV_AnticAleksa.pdf"));

            javaMailSender.send(mimeMessage);

            return "Success!";

        }catch (Exception e){
            return e.getMessage();
        }

    }

}
