package com.wato.watobackend.service;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImagesService {

    @Value("${image.upload.directory}")
    public String imageUploadDirectory;

    //    private final Resource defaultProfile;
    private final HttpHeaders headers = new HttpHeaders();

    public ResponseEntity<Resource> getImage(String type, String id, String filename) {
        String contentType = "image/png";
        Resource resource = null;
        try {
            Path path = Paths.get("images/"+ type+ "/" + id + "/" + filename).toAbsolutePath().normalize();
            resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                contentType = Files.probeContentType(path);
            }
        } catch (Exception e) {
            log.error("get profileImage message: {}", e.getMessage());
        }

        headers.add("Content-Type", contentType);

        if (!resource.exists()) {
            throw new ApiException(Error.NOT_EXIST_IMAGE);
//            return  ResponseEntity.ok()
//                    .headers(headers)
//                    .body(defaultProfile);
        }

        return  ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    public String getImageUploadDirectory() {
        return imageUploadDirectory;
    }
}
