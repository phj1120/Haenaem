package xyz.parkh.challenge.api.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.api.controller.user.model.request.UpdateUserInfoRequest;
import xyz.parkh.challenge.api.controller.user.model.response.GetUserInfoResponse;
import xyz.parkh.challenge.api.controller.user.model.response.UpdateUserInfoResponse;
import xyz.parkh.challenge.domain.user.entity.User;
import xyz.parkh.challenge.domain.user.service.UserService;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CHALLENGER', 'ADMIN')")
public class UserController {
    private final UserService userService;

    @GetMapping("/{authId}")
    public Result<GetUserInfoResponse> getUserInfo(@PathVariable("authId") String authId) {
        User user = userService.getUser(authId);

        return Result.<GetUserInfoResponse>builder()
                .data(new GetUserInfoResponse(user))
                .build();
    }

    @PatchMapping("/{authId}")
    public Result<UpdateUserInfoResponse> updateUserInfo(@PathVariable("authId") String authId,
                                                         @RequestBody @Validated UpdateUserInfoRequest request) {
        userService.update(request.convertUpdateUserDto(authId));
        User user = userService.getUser(authId);

        return Result.<UpdateUserInfoResponse>builder()
                .data(new UpdateUserInfoResponse(user))
                .build();
    }
}
