package xyz.parkh.challenge.domain.challenge.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;
import xyz.parkh.challenge.domain.challenge.model.ChallengePeriodType;
import xyz.parkh.challenge.domain.image.entity.Image;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_set_id")
    private Long id;

    private String name;

    private String info;

    private Long pointAmount; // 포인트

    private int subjectNumberOfWeek; // 주 당 제출할 과제

    @OneToOne
    @JoinColumn(name = "stored_name")
    private Image image;

    private String imageUrl;

    private String content;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private ChallengePeriodType periodType; // 일정 타입

    @Enumerated(value = EnumType.STRING)
    private ChallengeCategory category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChallengeSet)) return false;
        ChallengeSet that = (ChallengeSet) o;
        return subjectNumberOfWeek == that.subjectNumberOfWeek
                && Objects.equals(id, that.getId())
                && Objects.equals(name, that.getName())
                && Objects.equals(info, that.getInfo())
                && Objects.equals(pointAmount, that.getPointAmount())
                && Objects.equals(imageUrl, that.getImageUrl())
                && Objects.equals(content, that.getContent())
                && Objects.equals(description, that.getDescription())
                && periodType == that.periodType
                && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, info, pointAmount, subjectNumberOfWeek, imageUrl, content, description, periodType, category);
    }

    public ChallengeSet(String name, String info, Long pointAmount,
                        ChallengePeriodType periodType, int subjectNumberOfWeek, String imageUrl,
                        ChallengeCategory category, String content, String description) {
        this.name = name;
        this.info = info;
        this.pointAmount = pointAmount;
        this.periodType = periodType;
        this.subjectNumberOfWeek = subjectNumberOfWeek;
        this.imageUrl = imageUrl;
        this.category = category;
        this.content = content;
        this.description = description;
    }

}
