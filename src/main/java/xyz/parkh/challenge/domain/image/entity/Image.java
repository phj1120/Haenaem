package xyz.parkh.challenge.domain.image.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    private String storedName;
    private String originalName;
    private String storedPath;

    public Image(String originalName, String storedName, String storedPath) {
        this.storedName = storedName;
        this.originalName = originalName;
        this.storedPath = storedPath;
    }
}
