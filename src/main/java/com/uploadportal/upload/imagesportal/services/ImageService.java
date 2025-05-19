package com.uploadportal.upload.imagesportal.services;
import com.uploadportal.upload.imagesportal.dtos.UpdateStatus;
import com.uploadportal.upload.imagesportal.entities.ImageStatus;
import com.uploadportal.upload.imagesportal.entities.ImagesInfo;
import com.uploadportal.upload.imagesportal.exceptions.MissingFieldException;
import com.uploadportal.upload.imagesportal.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final RestTemplate restTemplate;

    @Value("${cloudflare.api.key}")
    private String CLOUDFLARE_API_KEY;

    @Value("${cloudflare.account.id}")
    private String ACCOUNT_ID;

    public List<ImagesInfo> uploadImage(ImagesInfo imagesInfo) {
        imagesInfo.setImageStatus(ImageStatus.active);
        return Collections.singletonList(imageRepository.save(imagesInfo));
    }

    public ImagesInfo getImageById(long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public ImagesInfo updateStatus(UpdateStatus updateStatus) {
        ImagesInfo image = imageRepository.findById(updateStatus.getId())
                .orElseThrow(() -> new RuntimeException("Image not found"));
        image.setImageStatus(ImageStatus.valueOf(updateStatus.getStatus()));
        return imageRepository.save(image);
    }

    public boolean deleteImage(Long imageId) {
        Optional<ImagesInfo> optionalImage = imageRepository.findById(imageId);
        if (optionalImage.isEmpty()) return false;

        ImagesInfo imagesInfo = optionalImage.get();
        String imageUrl = imagesInfo.getImageUrl();
        String cloudflareId = extractCloudflareIdFromUrl(imageUrl);
        if (cloudflareId == null) {
            return false;
        }

        String url = "https://api.cloudflare.com/client/v4/accounts/" + ACCOUNT_ID + "/images/v1/" + cloudflareId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + CLOUDFLARE_API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            imagesInfo.setImageStatus(ImageStatus.deleted);
            imageRepository.save(imagesInfo);
            return true;
        }

        return false;
    }
    public List<ImagesInfo> getAllImages() {
        List<ImagesInfo> images = imageRepository.findAll();

        Comparator<ImagesInfo> priorityComparator = Comparator.comparingInt(img -> switch (img.getPriority().toString()) {
            case "high" -> 1;
            case "medium" -> 2;
            case "low" -> 3;
            default -> 4;
        });

        images.sort(priorityComparator);
        return images;
    }
    public String extractCloudflareIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        if(parts.length > 4) {
            return parts[4];
        }
        throw new MissingFieldException(("Image Url is Invalid"));
    }

}
