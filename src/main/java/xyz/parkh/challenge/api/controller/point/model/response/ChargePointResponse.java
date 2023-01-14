package xyz.parkh.challenge.api.controller.point.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargePointResponse {
    private Long pointHistoryId;

    public ChargePointResponse(Long pointHistoryId) {
        this.pointHistoryId = pointHistoryId;
    }
}
