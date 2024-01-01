package com.wato.watobackend.controller;

import com.wato.watobackend.service.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/images")
@RestController
public class ImagesController {

    private final ImagesService imagesService;

    @GetMapping("/{type}/{id}/{filename}")
    public ResponseEntity<Resource> getImages(
            @PathVariable String type,
            @PathVariable String id,
            @PathVariable String filename)
    {
        return imagesService.getImage(type, id, filename);
    }
}
