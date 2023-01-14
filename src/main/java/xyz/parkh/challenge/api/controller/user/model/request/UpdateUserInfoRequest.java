package xyz.parkh.challenge.api.controller.user.model.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.user.model.UpdateUserDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserInfoRequest {
    private String changeName;
    private String changePassword;

    public UpdateUserDto convertUpdateUserDto(String targetAuthId) {
        return UpdateUserDto.builder()
                .targetAuthId(targetAuthId)
                .name(changeName)
                .password(changePassword)
                .build();
    }
}
