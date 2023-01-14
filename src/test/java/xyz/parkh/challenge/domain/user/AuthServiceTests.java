package xyz.parkh.challenge.domain.user;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.domain.challenge.ChallengeService;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;
import xyz.parkh.challenge.domain.challenge.model.AddChallengeSetDto;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;
import xyz.parkh.challenge.domain.challenge.repository.ChallengeRepository;
import xyz.parkh.challenge.domain.point.service.PointService;
import xyz.parkh.challenge.domain.user.entity.AuthKey;
import xyz.parkh.challenge.domain.user.entity.User;
import xyz.parkh.challenge.domain.user.model.*;
import xyz.parkh.challenge.domain.user.service.AuthService;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class AuthServiceTests {
    private final int TEST_SIZE = 100;
    private final AuthService authService;
    private final ChallengeService challengeService;
    private final PointService pointService;
    private final ChallengeRepository challengeRepository;

    @Test
    public void 회원가입() {
        SignUpUserDto signUpUserDto = new SignUpUserDto("authId", "password",
                "name", "AAA@AAA.com", UserType.ROLE_CHALLENGER);
        SignUpResultDto signUpResultDto = authService.signUp(signUpUserDto);
        User signUpUser = authService.findById(signUpResultDto.getId());

        Assertions.assertThat(signUpUserDto.getName()).isEqualTo(signUpUser.getName());
        Assertions.assertThat(signUpUserDto.getAuthId()).isEqualTo(signUpUser.getAuthId());
    }

    @Test
    public void 로그인() {
        SignUpUserDto signUpUserDto = new SignUpUserDto("authId", "password",
                "name", "AAA@AAA.com", UserType.ROLE_CHALLENGER);
        SignUpResultDto signUpResultDto = authService.signUp(signUpUserDto);
        User signUpUser = authService.findById(signUpResultDto.getId());

        SignInDto signInDto = new SignInDto("authId", "password");
        SignInResultDto signInResultDto = authService.signIn(signInDto);

        assertEquals(signUpUser.getId(), signInResultDto.getId());
        assertEquals(signUpUser.getAuthId(), signInResultDto.getAuthId());
    }

    @Test
    public void 회원탈퇴() {
        SignUpUserDto signUpUserDto = new SignUpUserDto("authId", "password",
                "name", "AAA@AAA.com", UserType.ROLE_CHALLENGER);
        SignUpResultDto signUpResultDto = authService.signUp(signUpUserDto);
        User signUpUser = authService.findById(signUpResultDto.getId());

        SignInDto signInDto = new SignInDto("authId", "password");
        SignInResultDto signInResultDto = authService.signIn(signInDto);
        Assertions.assertThat(signInResultDto.getAuthId()).isEqualTo(signUpUser.getAuthId());

        WithdrawalDto withdrawalDto = new WithdrawalDto("authId", "password");
        authService.withdrawal(withdrawalDto);

        Assertions.assertThatThrownBy(() -> authService.signIn(signInDto)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 회원탈퇴_포인트_충전기록이_있는_경우() {
        SignUpUserDto signUpUserDto = new SignUpUserDto("authId", "password",
                "name", "AAA@AAA.com", UserType.ROLE_CHALLENGER);
        SignUpResultDto signUpResultDto = authService.signUp(signUpUserDto);
        User signUpUser = authService.findById(signUpResultDto.getId());

        for (int i = 0; i < TEST_SIZE; i++) {
            signUpUser.chargePoint(10000L);
        }
        Assertions.assertThat(signUpUser.getPointHistories().size() == TEST_SIZE);

        WithdrawalDto withdrawalDto = new WithdrawalDto("authId", "password");
        authService.withdrawal(withdrawalDto);
    }

    @Test
    public void 회원탈퇴_챌린지_신청_기록이_있는_경우() {
        String authId = "authId" + UUID.randomUUID().toString().substring(0, 5);
        String password = "password";
        SignUpUserDto signUpUserDto = new SignUpUserDto(authId, password,
                "name", "AAA@AAA.com", UserType.ROLE_CHALLENGER);
        authService.signUp(signUpUserDto);

        SignInResultDto signInResultDto = authService.signIn(new SignInDto(signUpUserDto.getAuthId(), signUpUserDto.getPassword()));
        User user = authService.findById(signInResultDto.getId());

        AddChallengeSetDto dailyAddChallengeSetDto = AddChallengeSetDto.createForDaily("Daily" + UUID.randomUUID(), "info", 10000L,
                null, ChallengeCategory.LEARNING, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        ChallengeSet dailyChallengeSet = challengeService.addChallengeSet(dailyAddChallengeSetDto);

        Challenge dailyChallenge = Challenge.makeChallenge(dailyChallengeSet, "1-1", LocalDate.now());
        challengeService.addChallenge(dailyChallenge);

        user.chargePoint(100000L);
        pointService.chargePoint(user, 100000L);

        // 연관관계 편의 메서드로 챌린지 신청
        user.requestChallenge(dailyChallenge);

        // challengeService 로 챌린지 신청
//        challengeService.requestChallenge(dailyChallenge, user);

        //when : 회원 탈퇴
        WithdrawalDto withdrawalDto = new WithdrawalDto(authId, password);
        authService.withdrawal(withdrawalDto);

        // then : 회원 탈퇴 제대로 되었는지 확인
        Assertions.assertThatThrownBy(() -> authService.signIn(new SignInDto(authId, password)))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void 아이디_중복_체크() {
        String authId = "authId";
        boolean beforeSignUp = authService.checkUsableAuthId(authId);
        Assertions.assertThat(beforeSignUp).isTrue();

        SignUpUserDto signUpUserDto = new SignUpUserDto(authId, "password",
                "name", "AAA@AAA.com", UserType.ROLE_CHALLENGER);
        SignUpResultDto signUpResultDto = authService.signUp(signUpUserDto);
        User signUpUser = authService.findById(signUpResultDto.getId());

        boolean AfterSignUp = authService.checkUsableAuthId(authId);
        Assertions.assertThat(AfterSignUp).isFalse();
    }

    @Test
    public void 이메일_중복_체크() {
        String email = "authId";
        boolean beforeSignUp = authService.checkUsableEmail(email);
        Assertions.assertThat(beforeSignUp).isTrue();

        SignUpUserDto signUpUserDto = new SignUpUserDto("authId", "password",
                "name", email, UserType.ROLE_CHALLENGER);

        authService.signUp(signUpUserDto);

        boolean AfterSignUp = authService.checkUsableEmail(email);
        Assertions.assertThat(AfterSignUp).isFalse();
    }

    @Test
    public void 비밀번호_변경() {
        SignUpUserDto signUpUserDto = new SignUpUserDto("authId", "password",
                "name", "AAA@AAA.com", UserType.ROLE_CHALLENGER);
        SignUpResultDto signUpResultDto = authService.signUp(signUpUserDto);
        User signUpUser = authService.findById(signUpResultDto.getId());

        AuthKey authKey = authService.saveAuthKey(new AuthKeyDto(signUpUser.getAuthId(), signUpUser.getEmail(), MailAuthType.FORGET_PASSWORD));
        authService.changePassword(signUpUser.getId(), "changePassword", authKey.getAuthCode());

        SignInDto signInDto = new SignInDto(signUpUser.getAuthId(), "changePassword");
        SignInResultDto signInResultDto = authService.signIn(signInDto);
        assertEquals(signInDto.getAuthId(), signInResultDto.getAuthId());
    }

    @Test
    public void 인증_번호_생성() {

    }
}
