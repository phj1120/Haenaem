package xyz.parkh.challenge.domain.ui.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.ui.LinearStyleSector;
import xyz.parkh.challenge.domain.ui.StyleSector;

import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Linear extends ChallengeSetStyleSector {
    private String leftImage;
    private String centerLeftImage;
    private String centerRightImage;
    private String rightImage;

    public Linear(String leftImage, String centerLeftImage, String centerRightImage, String rightImage) {
        this.leftImage = leftImage;
        this.centerLeftImage = centerLeftImage;
        this.centerRightImage = centerRightImage;
        this.rightImage = rightImage;
    }

    @Override
    public StyleSector getStyleSector() {
        return new LinearStyleSector(leftImage, centerLeftImage, centerRightImage, rightImage);
    }

}
