package xyz.parkh.challenge.api.controller.challenge.model.request;

import lombok.Getter;
import lombok.Setter;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;
import xyz.parkh.challenge.domain.challenge.model.ChallengeProgressStatus;

import javax.validation.constraints.NotNull;

@Setter // Data Binding 에 필요
@Getter // Json 으로 변환에 필요
public class GetChallengesByCommonRequest {
    @NotNull(message = "{error.notnull}")
    ChallengeCategory category;
    @NotNull(message = "{error.notnull}")
    ChallengeProgressStatus status;
}
