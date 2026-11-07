package com.uploadportal.upload.imagesportal.controllers;

import com.uploadportal.upload.imagesportal.dtos.ImagesInfoDto;
import com.uploadportal.upload.imagesportal.dtos.UpdateCategory;
import com.uploadportal.upload.imagesportal.dtos.UpdateDescription;
import com.uploadportal.upload.imagesportal.dtos.UpdatePriority;
import com.uploadportal.upload.imagesportal.dtos.UpdateStatus;
import com.uploadportal.upload.imagesportal.entities.ImagesInfo;
import com.uploadportal.upload.imagesportal.entities.ProductCategory;
import com.uploadportal.upload.imagesportal.services.ImageService;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class ImagesController {

  private final ImageService imageService;

  @GetMapping("/get")
  public ResponseEntity<List<ImagesInfo>> getImages(Principal authentication) {
    return ResponseEntity.ok(imageService.getAllImages());
  }

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImagesInfo> uploadImages(
      @RequestParam("file") MultipartFile file,
      @ModelAttribute ImagesInfoDto imagesInfoDto,
      Principal principal) throws IOException {

    ImagesInfo savedImage = imageService.uploadImage(file, imagesInfoDto);
    return ResponseEntity.ok(savedImage);
  }
  @DeleteMapping("/{imageId}")
  public ResponseEntity<Boolean> deleteImage(@PathVariable Long imageId) {
    boolean deleted = imageService.deleteImage(imageId);
    return ResponseEntity.status(deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(deleted);
  }


  @GetMapping("/get/id/{id}")
  public ResponseEntity<ImagesInfo> getImageById(@PathVariable long id,
      Authentication authentication) {
    return ResponseEntity.ok(imageService.getImageById(id));
  }

  @PutMapping("/update/status")
  public ResponseEntity<ImagesInfo> updateImageStatus(@RequestBody UpdateStatus updateStatus,
      Principal authentication) {
    return ResponseEntity.ok(imageService.updateStatus(updateStatus));

  }

  @PutMapping("/update/priority")
  public ResponseEntity<ImagesInfo> updateImagePriority(@RequestBody UpdatePriority updatePriority,
      Authentication authentication) {
    return ResponseEntity.ok(imageService.updatePriority(updatePriority));
  }

  @PutMapping("/update/name/description")
  public ResponseEntity<ImagesInfo> updateDescriptionName(
      @RequestBody UpdateDescription updateDescription, Authentication authentication) {
    return ResponseEntity.ok(imageService.updateDescription(updateDescription));
  }

  @PutMapping("/update/category")
  public ResponseEntity<ImagesInfo> updateCategory(@RequestBody UpdateCategory updateCategory,
      Authentication authentication) {
    return ResponseEntity.ok(imageService.updateProductCategory(updateCategory));
  }

  //unauthenticated
  @GetMapping("/get/category")
  public ResponseEntity<List<ImagesInfo>> getCategory(
      @RequestBody ProductCategory productCategory) {
    return ResponseEntity.ok(imageService.getAllImagesByCategory(productCategory));
  }


}
