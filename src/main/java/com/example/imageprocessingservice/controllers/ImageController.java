package com.example.imageprocessingservice.controllers;

import com.example.imageprocessingservice.DTOs.Transformations;
import com.example.imageprocessingservice.models.Image;
import com.example.imageprocessingservice.repositories.ImageRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
class ImageController {
    private final ImageRepository imgRepository;

    ImageController(ImageRepository imgRepository) {
        this.imgRepository = imgRepository;
    }

    //Upload
    @PostMapping
    public ResponseEntity<Long> uploadImage(@RequestParam("file") MultipartFile file)
        throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setData(file.getBytes());
        image.setContentType(file.getContentType());
        Image savedImage = imgRepository.save(image);
        //@TODO: Return URL, Id and ContentType
        return ResponseEntity.ok(savedImage.getId());
    }

    //Download
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        return imgRepository.findById(id)
                .map(image -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                        .contentType(MediaType.parseMediaType(image.getContentType()))
                        .body(image.getData())
                )
                .orElse(ResponseEntity.status(404).body(null));
    }

    @PostMapping("/{id}/transform")
    public ResponseEntity<String> transformImage(
            @PathVariable Long id,
            @RequestBody Transformations transformations) {
        Integer width = transformations.resize().width();
        String format = transformations.format();
        return ResponseEntity.ok("Applied transformations to image " + id + " with format " + format + " and width " + width);
    }

}
