package com.uploadportal.upload.imagesportal.controllers;
import com.uploadportal.upload.imagesportal.dtos.UpdateStatus;
import com.uploadportal.upload.imagesportal.entities.ImagesInfo;
import com.uploadportal.upload.imagesportal.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor

public class ImagesController {
    private final ImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity<List<ImagesInfo>> uploadImages(@RequestBody ImagesInfo imagesInfo) {
        return ResponseEntity.ok(imageService.uploadImage(imagesInfo));
    }
    @GetMapping("/get")
    public ResponseEntity<List<ImagesInfo>> getImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }


    @GetMapping("/get/id/{id}")
    public ResponseEntity<ImagesInfo> getImageById(@PathVariable long id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }
    @PutMapping("/update/status")
    public ResponseEntity<ImagesInfo> updateImageStatus(@RequestBody UpdateStatus updateStatus) {
        return ResponseEntity.ok(imageService.updateStatus(updateStatus));

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable long id) {
        boolean success = imageService.deleteImage(id);

        if (success) {
            return ResponseEntity.ok("Image deleted and status updated");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Deletion failed");
        }
    }

}
