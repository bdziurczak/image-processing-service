package com.example.imageprocessingservice.controllers;

import com.example.imageprocessingservice.DTOs.Transformations;
import com.example.imageprocessingservice.models.Image;
import com.example.imageprocessingservice.repositories.ImageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/images")
class ImageController {
    private final ImageRepository imgRepository;

    ImageController(ImageRepository imgRepository) {
        this.imgRepository = imgRepository;
    }

    @Operation(
            summary = "Upload an image",
            description = "Uploads an image file and stores it in the database. Returns the image ID."
    )
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

    @Operation(summary = "Download image by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully",
                    content = @Content(mediaType = "image/*",  schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
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

    @Operation(summary = "Apply transformations to an image")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transformations applied successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> transformImage(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transformations to apply",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Transformations.class))
            )
            @RequestBody Transformations transformations) {
        Integer width = transformations.resize().width();
        String format = transformations.format();
        return ResponseEntity.ok("Applied transformations to image " + id + " with format " + format + " and width " + width);
    }

    @Operation(summary = "Create a transformed copy of an image")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Copy created successfully"),
            @ApiResponse(responseCode = "404", description = "Original image not found")
    })
    @PostMapping("/{id}/copies")
    public ResponseEntity<Long> copyAndTransformImage(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transformations to apply",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Transformations.class))
            )
            @RequestBody Transformations transformations) throws IOException {
        return imgRepository.findById(id).map(original -> {
            Image newImage = new Image();
            newImage.setName("copy-of-" + original.getName());
            newImage.setContentType(transformations.format() != null
                    ? transformations.format()
                    : original.getContentType());

            // TODO: apply actual transformation logic (resize, convert format, etc.)
            newImage.setData(original.getData());

            Image saved = imgRepository.save(newImage);

            return ResponseEntity
                    .created(URI.create("/api/images/" + saved.getId()))
                    .body(saved.getId());
        }).orElse(ResponseEntity.status(404).build());
    }

}
