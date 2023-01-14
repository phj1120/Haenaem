package xyz.parkh.challenge.api.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.api.controller.user.model.request.*;
import xyz.parkh.challenge.api.controller.user.model.response.*;
import xyz.parkh.challenge.exception.ErrorCode;
import xyz.parkh.challenge.domain.user.entity.User;
import xyz.parkh.challenge.domain.user.model.SignInResultDto;
import xyz.parkh.challenge.domain.user.model.SignUpResultDto;
import xyz.parkh.challenge.domain.user.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public Result<SignUpResponse> signUp(@RequestBody @Validated SignUpRequest signUpRequest) {
        SignUpResultDto signUpResultDto = authService.signUp(signUpRequest.convertDto());
        User user = authService.findById(signUpResultDto.getId());

        return Result.<SignUpResponse>builder().data(new SignUpResponse(user)).build();
    }

    @PostMapping("/sign-in")
    public Result<SignInResponse> signIn(@RequestBody @Validated SignInRequest signInRequest) {
        SignInResultDto signInResultDto = authService.signIn(signInRequest.convertSignInDto());

        return Result.<SignInResponse>builder().data(new SignInResponse(signInResultDto)).build();
    }

    @PostMapping("/withdrawal")
    @PreAuthorize("hasAnyRole('CHALLENGER')")
    public Result<WithdrawalResponse> withdrawal(@RequestBody @Validated WithdrawalRequest withdrawalRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authId = authentication.getName();
        User user = authService.findByAuthId(authId);

        if (user.getAuthId() == withdrawalRequest.getAuthId()) {
            throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED.getMessage());
        }
        authService.withdrawal(withdrawalRequest.convertWithdrawal());

        return Result.<WithdrawalResponse>builder().data(new WithdrawalResponse(true)).build();
    }

    @GetMapping("/check/auth-id/{authId}")
    public Result<CheckUsableAuthIdResponse> checkUsableAuthId(@PathVariable("authId") String authId) {
        boolean isUsable = authService.checkUsableAuthId(authId);

        return Result.<CheckUsableAuthIdResponse>builder().data(new CheckUsableAuthIdResponse(isUsable)).build();
    }

    @GetMapping("/check/email/{email}")
    public Result<CheckUsableEmailResponse> checkUsableEmail(@PathVariable("email") String email) {
        boolean isUsable = authService.checkUsableEmail(email);

        return Result.<CheckUsableEmailResponse>builder().data(new CheckUsableEmailResponse(isUsable)).build();
    }

    @PostMapping("/reset/password")
    public Result<ChangePasswordResponse> changePassword(@RequestBody @Validated ChangePasswordRequest request) {
        User user = authService.findById(request.getUserId());
        authService.changePassword(request.getUserId(), request.getNewPassword(), request.getAuthCode());

        return Result.<ChangePasswordResponse>builder().data(new ChangePasswordResponse(user.getId())).build();
    }

    @PostMapping("/send/auth-key")
    public Result<SendAuthKeyResponse> sendAuthKey(@RequestBody @Validated SendAuthKeyRequest request) {
        boolean state = authService.sendAuthKey(request.convertAuthKeyDto());
        return Result.<SendAuthKeyResponse>builder().data(new SendAuthKeyResponse(state)).build();
    }

    @PostMapping("/check/auth-code")
    public Result<CheckAuthKeyResponse> checkAuthKey(@RequestBody @Validated CheckAuthKeyRequest request) {
        boolean status = authService.checkAuthKey(request.getAuthId(), request.getEmail(), request.getAuthCode(), request.getType());

        return Result.<CheckAuthKeyResponse>builder().data(new CheckAuthKeyResponse(status)).build();
    }
}