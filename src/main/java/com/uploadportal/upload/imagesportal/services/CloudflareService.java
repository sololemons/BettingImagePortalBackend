package com.uploadportal.upload.imagesportal.services;
import com.uploadportal.upload.imagesportal.exceptions.MissingFieldException;
import com.uploadportal.upload.imagesportal.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CloudflareService {
    private final ImageRepository imageRepository;

    @Value("${cloudflare.api.key}")
    private String CLOUDFLARE_API_KEY;

    @Value("${cloudflare.account.id}")
    private String ACCOUNT_ID;


    public String uploadToCloudflare(MultipartFile file) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String boundary = "----WebKitFormBoundary";
        HttpRequest.BodyPublisher body = buildMultipartBody(file, boundary);
        String CLOUDFLARE_URL = "https://api.cloudflare.com/client/v4/accounts/" + ACCOUNT_ID + "/images/v1";


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CLOUDFLARE_URL))
                .header("Authorization", "Bearer " + CLOUDFLARE_API_KEY)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(body)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new MissingFieldException("Failed to upload image. Status: " + response.statusCode() + ", Body: " + response.body());
        }

        return response.body();
    }

    private HttpRequest.BodyPublisher buildMultipartBody(MultipartFile file, String boundary) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8), true);

        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                .append(file.getOriginalFilename()).append("\"\r\n");
        writer.append("Content-Type: ").append(file.getContentType()).append("\r\n\r\n");
        writer.flush();

        baos.write(file.getBytes());
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8));
        baos.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        return HttpRequest.BodyPublishers.ofByteArray(baos.toByteArray());
    }


}
