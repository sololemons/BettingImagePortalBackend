package com.uploadportal.upload.imagesportal.controllers;
import com.uploadportal.upload.imagesportal.dtos.UpdateCategory;
import com.uploadportal.upload.imagesportal.dtos.UpdateDescription;
import com.uploadportal.upload.imagesportal.dtos.UpdatePriority;
import com.uploadportal.upload.imagesportal.dtos.UpdateStatus;
import com.uploadportal.upload.imagesportal.entities.ImagesInfo;
import com.uploadportal.upload.imagesportal.entities.ProductCategory;
import com.uploadportal.upload.imagesportal.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")


public class ImagesController {
    private final ImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity<List<ImagesInfo>> uploadImages(@RequestBody ImagesInfo imagesInfo, Authentication authentication) {
        return ResponseEntity.ok(imageService.uploadImage(imagesInfo));
    }
    @GetMapping("/get")
    public ResponseEntity<List<ImagesInfo>> getImages(Authentication authentication) {
        return ResponseEntity.ok(imageService.getAllImages());
    }


    @GetMapping("/get/id/{id}")
    public ResponseEntity<ImagesInfo> getImageById(@PathVariable long id,Authentication authentication) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }
    @PutMapping("/update/status")
    public ResponseEntity<ImagesInfo> updateImageStatus(@RequestBody UpdateStatus updateStatus,Authentication authentication) {
        return ResponseEntity.ok(imageService.updateStatus(updateStatus));

    }
    @PutMapping("/update/priority")
    public ResponseEntity<ImagesInfo> updateImagePriority(@RequestBody UpdatePriority updatePriority,Authentication authentication) {
        return ResponseEntity.ok(imageService.updatePriority(updatePriority));
    }
    @PutMapping("/update/name/description")
    public ResponseEntity<ImagesInfo> updateDescriptionName(@RequestBody UpdateDescription updateDescription,Authentication authentication) {
        return ResponseEntity.ok(imageService.updateDescription(updateDescription));
    }
    @PutMapping("/update/category")
    public ResponseEntity<ImagesInfo> updateCategory(@RequestBody UpdateCategory updateCategory,Authentication authentication) {
        return ResponseEntity.ok(imageService.updateProductCategory(updateCategory));
    }
    //unauthenticated
    @GetMapping("/get/category")
    public ResponseEntity<List<ImagesInfo>> getCategory(@RequestBody ProductCategory productCategory) {
        return ResponseEntity.ok(imageService.getAllImagesByCategory(productCategory));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable long id, Authentication authentication) {
        boolean success = imageService.deleteImage(id);

        if (success) {
            return ResponseEntity.ok("Image deleted and status updated");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Deletion failed");
        }
    }

}
