package com.uploadportal.upload.imagesportal.repositories;
import com.uploadportal.upload.imagesportal.entities.ImagesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImagesInfo, Long> {
}
