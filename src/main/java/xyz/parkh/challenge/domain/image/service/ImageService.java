package xyz.parkh.challenge.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.parkh.challenge.exception.ErrorCode;
import xyz.parkh.challenge.domain.image.entity.Image;
import xyz.parkh.challenge.domain.image.repository.ImageRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${path.image.challenge}")
    private String challengeDir;

    @Value("${path.image.task}")
    private String taskDir;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Path.of(challengeDir));
        Files.createDirectories(Path.of(taskDir));
    }

    public String getStoredPath(String storedName) {
        Image image = imageRepository.findById(storedName).orElseThrow();
        return image.getStoredPath();
    }

    // 파일 저장
    public String save(String folderPath, MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String storedName = makeNewName(file.getOriginalFilename());
        String storedPath = folderPath + storedName;
        file.transferTo(new File(storedPath));

        Image image = new Image(originalName, storedName, storedPath);
        imageRepository.save(image);

        return storedName;
    }

    // 챌린지 이미지 저장
    public String saveChallengeImage(MultipartFile file) throws IOException {
        return save(challengeDir, file);
    }

    // 과제 이미지 저장
    public String saveTask(MultipartFile file) throws IOException {
        return save(taskDir, file);
    }

    // 기존 파일 이름 이용해 서버에 저장 될 이름 생성
    private String makeNewName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String[] splitFileName = originalFilename.split("[.]");
        String fileType = splitFileName[splitFileName.length - 1];

        return uuid + "." + fileType;
    }

    public Resource getResource(String storedName) {
        String storedPath = getStoredPath(storedName);
        UrlResource resource;
        try {
            resource = new UrlResource("file:" + storedPath);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(ErrorCode.FILE_ERROR.getMessage());
        }
        return resource;
    }

    public String getChallengeDir() {
        return challengeDir;
    }

    public void save(Image image) {

    }
}
