package xyz.parkh.challenge.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.exception.ErrorCode;
import xyz.parkh.challenge.jwt.TokenProvider;
import xyz.parkh.challenge.domain.user.entity.AuthKey;
import xyz.parkh.challenge.domain.user.entity.User;
import xyz.parkh.challenge.domain.user.model.*;
import xyz.parkh.challenge.domain.user.repository.AuthKeyRepository;
import xyz.parkh.challenge.domain.user.repository.AuthorityRepository;
import xyz.parkh.challenge.domain.user.repository.UserRepository;
import xyz.parkh.challenge.utils.email.MailService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthKeyRepository authKeyRepository;
    private final AuthorityRepository authorityRepository;
    private final MailService mailService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    public SignUpResultDto signUp(SignUpUserDto signUpUserDto) {
        User user = User.make(signUpUserDto.getAuthId(), passwordEncoder.encode(signUpUserDto.getPassword()),
                signUpUserDto.getName(), signUpUserDto.getEmail(), signUpUserDto.getUserType());
        User savedUser = userRepository.save(user);
        SignUpResultDto signUpResultDto = new SignUpResultDto(savedUser);

        return signUpResultDto;
    }

//    public User signUp(User user, UserType userType) {
//        Authority authority = Authority.make(user);
//        authorityRepository.save(authority);
//
//        User saveUser = userRepository.save(user);
//
//        return saveUser;
//    }

    public SignInResultDto signIn(SignInDto signInDto) {
        User existUser = userRepository.findOneWithAuthoritiesByAuthId(signInDto.getAuthId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_AUTH_ID.getMessage()));
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(signInDto.getAuthId(), signInDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = tokenProvider.createToken(authentication);
            return new SignInResultDto(existUser, token);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException(ErrorCode.DIFFERENCE_PASSWORD.getMessage());
        }
    }

    public User changePassword(Long userId, String newPassword, String authKey) {
        User user = findById(userId);

        if (!checkAuthKey(user.getAuthId(), user.getEmail(), authKey, MailAuthType.FORGET_PASSWORD)) {
            throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED.getMessage());
        }

        user.changePassword(passwordEncoder.encode(newPassword));
        return user;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.NO_ID.getMessage()));
    }

    public User findByAuthId(String authId) {
        return userRepository.findByAuthId(authId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.NO_AUTH_ID.getMessage()));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.NO_EMAIL.getMessage()));
    }

    public User withdrawal(WithdrawalDto withdrawalDto) {
        User existUser = findByAuthId(withdrawalDto.getAuthId());

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(withdrawalDto.getAuthId(), withdrawalDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            userRepository.delete(existUser);
            return existUser;
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException(ErrorCode.DIFFERENCE_PASSWORD.getMessage());
        }
    }

    public boolean checkUsableAuthId(String authId) {
        User existUser = userRepository.findByAuthId(authId).orElse(null);
        if (existUser == null) {
            return true;
        }
        return false;
    }

    public boolean checkUsableEmail(String email) {
        User existUser = userRepository.findByEmail(email).orElse(null);
        if (existUser == null) {
            return true;
        }
        return false;
    }

    public boolean sendAuthKey(AuthKeyDto authKeyDto) {
        AuthKey authKey = saveAuthKey(authKeyDto);
        SendAuthKeyDto sendAuthKeyDto = new SendAuthKeyDto(authKey);
        mailService.sendAuthKey(sendAuthKeyDto);
        return true;
    }

    public AuthKey saveAuthKey(AuthKeyDto authKeyDto) {
        // 비밀번호 찾기 시 저장된 이메일과 같은지 확인
        if (authKeyDto.getType() == MailAuthType.FORGET_PASSWORD) {
            String authId = authKeyDto.getAuthId();
            User byAuthId = findByAuthId(authId);
            if (!authKeyDto.getEmail().equals(byAuthId.getEmail())) {
                throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED.getMessage());
            }
        }

        if (authKeyDto.getType() == MailAuthType.SIGN_UP) {
            String authId = authKeyDto.getAuthId();
            String email = authKeyDto.getEmail();
            if (!checkUsableEmail(email)) {
                throw new IllegalArgumentException(ErrorCode.EXIST_EMAIL.getMessage());
            }
            if (!checkUsableAuthId(authId)) {
                throw new IllegalArgumentException(ErrorCode.EXIST_AUTH_ID.getMessage());
            }
        }

        return authKeyRepository.save(new AuthKey(authKeyDto));
    }

    public boolean checkAuthKey(String authId, String email, String authKey, MailAuthType type) {
        AuthKey recentAuthKey = authKeyRepository.findTopByEmailAndTypeOrderByCreateTimeDesc(email, type).orElse(null);
        if (recentAuthKey == null) {
            return false;
        }

        if (!email.equals(recentAuthKey.getEmail())) {
            throw new IllegalArgumentException(ErrorCode.DIFFERENCE_EMAIL.getMessage());
        }

        if (!authId.equals(recentAuthKey.getAuthId())) {
            throw new IllegalArgumentException(ErrorCode.DIFFERENCE_AUTH_ID.getMessage());
        }

        // 10분 이내에 발송된 코드만 인정
        LocalDateTime limitRecognitionLocalDateTime = recentAuthKey.getCreateTime().plusMinutes(10);
        if (LocalDateTime.now().isBefore(limitRecognitionLocalDateTime)) {
            if (authKey.equals(recentAuthKey.getAuthCode())) {
                return true;
            }
        }

        return false;
    }
}
