package xyz.parkh.challenge.domain.user.model;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateUserDto {
    private String targetAuthId;
    private String name;

    private String password;


    @Builder
    public UpdateUserDto(String targetAuthId, String name, String password) {
        this.targetAuthId = targetAuthId;
        this.name = name;
        this.password = password;
    }
}
