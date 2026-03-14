package com.example.identityfamily.core.domain.globalservice;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) throws MessagingException {

        String subject = "Email Verification Code";
        String html = buildVerificationTemplate(code);

        sendHtmlEmail(to, subject, html);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }

    private String buildVerificationTemplate(String code) {

        return """
                <html>
                    <body style="font-family: Arial; background-color:#f4f4f4; padding:20px;">
                        <div style="max-width:500px; margin:auto; background:white; padding:30px; border-radius:10px; text-align:center;">
                            
                            <h2 style="color:#333;">Verify Your Email</h2>
                            
                            <p style="font-size:16px; color:#555;">
                                Use the verification code below to complete your registration.
                            </p>

                            <div style="font-size:28px; font-weight:bold; color:#2c7be5; margin:20px 0;">
                                %s
                            </div>

                            <p style="font-size:14px; color:#777;">
                                This code will expire in 5 minutes.
                            </p>

                        </div>
                    </body>
                </html>
                """.formatted(code);
    }
}
