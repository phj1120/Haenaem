package xyz.parkh.challenge.domain.ui;

import lombok.Getter;

@Getter
public class GridStyleSector implements StyleSector {
    private String leftUpImage;
    private String rightUpImage;
    private String leftDownImage;
    private String rightDownImage;

    public GridStyleSector(String leftUpImage, String rightUpImage, String leftDownImage, String rightDownImage) {
        this.leftUpImage = leftUpImage;
        this.rightUpImage = rightUpImage;
        this.leftDownImage = leftDownImage;
        this.rightDownImage = rightDownImage;
    }
}

