package xyz.parkh.challenge.api.controller.point.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAmountResponse {
    private Long pointAmount;

    public GetAmountResponse(Long pointAmount) {
        this.pointAmount = pointAmount;
    }
}
