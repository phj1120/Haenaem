package xyz.parkh.challenge.api.controller.challenge.model;

import lombok.Getter;
import xyz.parkh.challenge.domain.user.entity.User;

@Getter
public class UserInfo {
    long id;
    String authId;
    String name;

    public UserInfo(User user) {
        id = user.getId();
        authId = user.getAuthId();
        name = user.getName();
    }
}
