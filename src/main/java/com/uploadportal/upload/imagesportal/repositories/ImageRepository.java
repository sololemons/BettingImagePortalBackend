package com.uploadportal.upload.imagesportal.repositories;
import com.uploadportal.upload.imagesportal.entities.ImagesInfo;
import com.uploadportal.upload.imagesportal.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImagesInfo, Long> {
    List<ImagesInfo> findByProductCategory(ProductCategory productCategory);
}
