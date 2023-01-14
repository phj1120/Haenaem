package xyz.parkh.challenge.api.controller.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.parkh.challenge.domain.image.service.ImageService;

@Slf4j
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;


    /**
     * 이미지 조회
     *
     * @param storedName 저장된 이미지 이름
     * @return 이미지
     */
    @GetMapping(path = "/{storedName}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getChallengeImage(@PathVariable("storedName") String storedName) {
        return imageService.getResource(storedName);
    }
}
