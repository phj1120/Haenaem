package xyz.parkh.challenge.utils.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.parkh.challenge.domain.user.model.SendAuthKeyDto;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    @Async
    public void sendSimpleMessage(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmailAddress);
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());
        emailSender.send(message);
    }

    @Async
    public void sendAuthKey(SendAuthKeyDto sendAuthKeyDto) {
        String authCode = sendAuthKeyDto.getAuthCode();
        String email = sendAuthKeyDto.getEmail();
        String authId = sendAuthKeyDto.getAuthId();
        String title = "해냄 인증 요청";
        String content = authId + " 님 안녕하세요.\n" +
                "해냄 확인 코드 : " + authCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmailAddress);
        message.setTo(email);
        message.setSubject(title);
        message.setText(content);
        emailSender.send(message);
    }
}
