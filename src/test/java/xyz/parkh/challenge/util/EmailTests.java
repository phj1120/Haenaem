package xyz.parkh.challenge.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.parkh.challenge.utils.email.MailDto;
import xyz.parkh.challenge.utils.email.MailService;

@SpringBootTest
@RequiredArgsConstructor
public class EmailTests {
    private final MailService mailService;

    //    @Test
    public void 이메일_전송() {
        MailDto mailDto = new MailDto("hyeonj1998@naver.com", "해냄 테스트 중", "테스트 실행 중");
        mailService.sendSimpleMessage(mailDto);
    }
}
