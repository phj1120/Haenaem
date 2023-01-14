package xyz.parkh.challenge.domain.challenge.model;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class AddChallengeSetDto {
    private String name;
    private String info;
    private Long pointAmount;
    private MultipartFile image;
    private ChallengeCategory category;
    private String content;
    private String description;
    private ChallengePeriodType periodType;
    private int subjectNumberOfWeek;

    private AddChallengeSetDto(String name, String info, Long pointAmount, MultipartFile image,
                               ChallengeCategory category, String content, String description,
                               ChallengePeriodType periodType, int subjectNumberOfWeek) {
        this.name = name;
        this.info = info;
        this.pointAmount = pointAmount;
        this.image = image;
        this.category = category;
        this.content = content;
        this.description = description;
        this.periodType = periodType;
        this.subjectNumberOfWeek = subjectNumberOfWeek;
    }


    public static AddChallengeSetDto createForWeekly(String name, String info, Long pointAmount, MultipartFile image,
                                                              ChallengeCategory category, String content, String description,
                                                              ChallengePeriodType periodType, int subjectNumberOfWeek) {
        return new AddChallengeSetDto(name, info, pointAmount, image, category, content, description, periodType, subjectNumberOfWeek);
    }

    public static AddChallengeSetDto createForDaily(String name, String info, Long pointAmount, MultipartFile image,
                                                             ChallengeCategory category, String content, String description) {
        return new AddChallengeSetDto(name, info, pointAmount, image, category, content, description, ChallengePeriodType.DAILY, 1);
    }

}
