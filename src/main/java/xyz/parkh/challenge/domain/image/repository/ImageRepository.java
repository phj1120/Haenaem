package xyz.parkh.challenge.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.parkh.challenge.domain.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

}
