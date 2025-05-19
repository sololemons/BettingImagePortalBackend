package com.uploadportal.upload.imagesportal.controllers;
import com.uploadportal.upload.imagesportal.services.CloudflareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/cloudflare")
@RequiredArgsConstructor
public class CloudflareController {

    private final CloudflareService cloudflareService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String result = cloudflareService.uploadToCloudflare(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

}
