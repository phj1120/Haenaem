package xyz.parkh.challenge.api.controller.challenge.model.request;

import lombok.Getter;
import lombok.Setter;
import xyz.parkh.challenge.domain.challenge.model.HistoryType;

@Getter
@Setter
public class GetUsersRequest {
    private long id;
    private HistoryType type;

    public GetUsersRequest(long id, HistoryType type) {
        this.id = id;
        this.type = type;
    }
}
