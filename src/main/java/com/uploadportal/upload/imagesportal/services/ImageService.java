package com.uploadportal.upload.imagesportal.services;

import com.uploadportal.upload.imagesportal.dtos.ImagesInfoDto;
import com.uploadportal.upload.imagesportal.dtos.UpdateCategory;
import com.uploadportal.upload.imagesportal.dtos.UpdateDescription;
import com.uploadportal.upload.imagesportal.dtos.UpdatePriority;
import com.uploadportal.upload.imagesportal.dtos.UpdateStatus;
import com.uploadportal.upload.imagesportal.entities.ImageStatus;
import com.uploadportal.upload.imagesportal.entities.ImagesInfo;
import com.uploadportal.upload.imagesportal.entities.ProductCategory;
import com.uploadportal.upload.imagesportal.exceptions.MissingFieldException;
import com.uploadportal.upload.imagesportal.repositories.ImageRepository;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageRepository imageRepository;
  private final S3Client s3Client;
  private final SnsClient snsClient;

  @Value("${aws.s3.bucket-name}")
  private  String bucketName;

  @Value("${aws.sns.topic-arn}")
  private  String topicArn;

  public ImagesInfo getImageById(long id) {
    return imageRepository.findById(id).orElse(null);
  }

  public ImagesInfo uploadImage(MultipartFile file, ImagesInfoDto imagesInfoDto) throws IOException {
    String s3Key = UUID.randomUUID() + "-" + file.getOriginalFilename();

    // 1. Upload image binary directly to S3
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

    ImagesInfo imagesInfo = new ImagesInfo();
    imagesInfo.setImageUrl(s3Key);
    imagesInfo.setDescription(imagesInfoDto.getDescription());
    imagesInfo.setImageName(imagesInfoDto.getImageName());
    imagesInfo.setProductCategory(ProductCategory.valueOf(imagesInfoDto.getProductCategory()));
    imagesInfo.setImageStatus(ImageStatus.active);
    ImagesInfo savedImage = imageRepository.save(imagesInfo);

    String subject = "Image Uploaded: " + file.getOriginalFilename();
    String message = String.format(
        "An image has been successfully uploaded to the portal.\n\n" +
            "Details:\n" +
            "- Image ID: %d\n" +
            "- File Name: %s\n" +
            "- S3 Key: %s\n" +
            "- Bucket: %s",
        savedImage.getId(), file.getOriginalFilename(), s3Key, bucketName
    );

    publishEmailNotification(subject, message);

    return savedImage;
  }

  public boolean deleteImage(Long imageId) {
    Optional<ImagesInfo> optionalImage = imageRepository.findById(imageId);
    if (optionalImage.isEmpty()) {
      return false;
    }

    ImagesInfo imagesInfo = optionalImage.get();
    String s3Key = extractS3KeyFromUrl(imagesInfo.getImageUrl());

    if (s3Key == null || s3Key.isBlank()) {
      return false;
    }

    // 1. Delete binary object from S3
    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    s3Client.deleteObject(deleteObjectRequest);

    // 2. Update status to DELETED in Database
    imagesInfo.setImageStatus(ImageStatus.deleted);
    imageRepository.save(imagesInfo);

    String subject = "Image Deleted: ID " + imageId;
    String message = String.format(
        "An image has been removed from the portal.\n\n" +
            "Details:\n" +
            "- Image ID: %d\n" +
            "- S3 Key: %s\n" +
            "- Bucket: %s",
        imageId, s3Key, bucketName
    );

    publishEmailNotification(subject, message);

    return true;
  }

  private String extractS3KeyFromUrl(String imageUrl) {
    if (imageUrl == null || imageUrl.isBlank()) {
      return null;
    }
    return imageUrl.contains("/") ? imageUrl.substring(imageUrl.lastIndexOf("/") + 1) : imageUrl;
  }
  private void publishEmailNotification(String subject, String message) {
    PublishRequest request = PublishRequest.builder()
        .topicArn(topicArn)
        .subject(subject)
        .message(message)
        .build();

    snsClient.publish(request);
  }

  public ImagesInfo updateStatus(UpdateStatus updateStatus) {

    ImagesInfo image = imageRepository.findById(updateStatus.getId())
        .orElseThrow(() -> new MissingFieldException("Image not found"));
    image.setImageStatus(ImageStatus.valueOf(updateStatus.getStatus()));
    return imageRepository.save(image);
  }

  public List<ImagesInfo> getAllImages() {
    List<ImagesInfo> images = imageRepository.findAll();
    images.sort(Comparator.comparingInt(ImagesInfo::getPriority).reversed());
    return images;
  }

  public String extractCloudflareIdFromUrl(String imageUrl) {
    String[] parts = imageUrl.split("/");
    if (parts.length > 4) {
      return parts[4];
    }
    throw new MissingFieldException(("Image Url is Invalid"));
  }

  public ImagesInfo updatePriority(UpdatePriority updatePriority) {
    ImagesInfo image = imageRepository.findById(updatePriority.getId())
        .orElseThrow(() -> new MissingFieldException("Image not found"));
    image.setPriority(updatePriority.getPriority());
    return imageRepository.save(image);
  }

  public ImagesInfo updateDescription(UpdateDescription updateDescription) {
    ImagesInfo imagesInfo = imageRepository.findById(updateDescription.getId())
        .orElseThrow(() -> new MissingFieldException("Image not found"));
    imagesInfo.setDescription(updateDescription.getDescription());
    imagesInfo.setImageName(updateDescription.getImageName());
    return imageRepository.save(imagesInfo);
  }

  public ImagesInfo updateProductCategory(UpdateCategory updateCategory) {
    ImagesInfo imagesInfo = imageRepository.findById(updateCategory.getId())
        .orElseThrow(() -> new MissingFieldException("Image not found"));
    imagesInfo.setProductCategory(ProductCategory.valueOf(updateCategory.getCategory()));
    return imageRepository.save(imagesInfo);
  }

  public List<ImagesInfo> getAllImagesByCategory(ProductCategory productCategory) {
    List<ImagesInfo> images = imageRepository.findByProductCategory(productCategory);
    images.sort(Comparator.comparingInt(ImagesInfo::getPriority).reversed());
    return images;
  }
}
