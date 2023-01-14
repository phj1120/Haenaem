package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;
import xyz.parkh.challenge.api.controller.challenge.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetUsersResponse {
    List<UserInfo> users = new ArrayList<>();

    public GetUsersResponse(List<UserInfo> users) {
        this.users = users;
    }
}
