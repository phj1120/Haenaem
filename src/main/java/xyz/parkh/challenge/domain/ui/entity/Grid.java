package xyz.parkh.challenge.domain.ui.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.parkh.challenge.domain.ui.GridStyleSector;
import xyz.parkh.challenge.domain.ui.StyleSector;

import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grid extends ChallengeSetStyleSector {
    private String leftUpImage;
    private String rightUpImage;
    private String leftDownImage;
    private String rightDownImage;

    public Grid(String leftUpImage, String rightUpImage, String leftDownImage, String rightDownImage) {
        this.leftUpImage = leftUpImage;
        this.rightUpImage = rightUpImage;
        this.leftDownImage = leftDownImage;
        this.rightDownImage = rightDownImage;
    }

    @Override
    public StyleSector getStyleSector() {
        return new GridStyleSector(leftUpImage, rightUpImage, leftDownImage, rightDownImage);
    }
}
