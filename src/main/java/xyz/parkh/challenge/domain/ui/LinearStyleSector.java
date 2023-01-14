package xyz.parkh.challenge.domain.ui;

import lombok.Getter;

@Getter
public class LinearStyleSector implements StyleSector {
    private String leftImage;
    private String centerLeftImage;
    private String centerRightImage;
    private String rightImage;

    public LinearStyleSector(String leftImage, String centerLeftImage, String centerRightImage, String rightImage) {
        this.leftImage = leftImage;
        this.centerLeftImage = centerLeftImage;
        this.centerRightImage = centerRightImage;
        this.rightImage = rightImage;
    }
}
