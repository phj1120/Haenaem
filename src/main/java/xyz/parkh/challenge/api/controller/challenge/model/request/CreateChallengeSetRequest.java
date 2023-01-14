package xyz.parkh.challenge.api.controller.challenge.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import xyz.parkh.challenge.domain.challenge.model.AddChallengeSetDto;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;
import xyz.parkh.challenge.domain.challenge.model.ChallengePeriodType;

@Setter
@Getter
public class CreateChallengeSetRequest {
    private String name;
    private String info;
    private Long pointAmount;
    private MultipartFile image;
    private ChallengeCategory category;
    private String content;
    private String description;
    private ChallengePeriodType periodType;
    private int subjectNumberOfWeek;

    public AddChallengeSetDto convertDto() {
        if (periodType == ChallengePeriodType.DAILY) {
            return AddChallengeSetDto.createForDaily(name, info, pointAmount, image, category, content, description);
        }
        return AddChallengeSetDto.createForWeekly(name, info, pointAmount, image, category, content, description, periodType, subjectNumberOfWeek);
    }
}
