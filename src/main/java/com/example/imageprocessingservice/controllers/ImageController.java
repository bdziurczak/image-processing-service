package com.example.imageprocessingservice.controllers;

import com.example.imageprocessingservice.DTOs.ImageResponse;
import com.example.imageprocessingservice.DTOs.Transformations;
import com.example.imageprocessingservice.TableModels.ImageOriginal;
import com.example.imageprocessingservice.repositories.ImageRepository;
import com.example.imageprocessingservice.utils.TransformImage;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam MultipartFile file)
        throws IOException {
        ImageOriginal image = new ImageOriginal();
        image.setName(file.getOriginalFilename());
        image.setData(file.getBytes());
        image.setContentType(file.getContentType());
        ImageOriginal savedImage = imgRepository.save(image);
        //@TODO: Return URL, Id and ContentType
        String url = "/images/" + savedImage.getId();

        ImageResponse response = new ImageResponse(
                savedImage.getId(),
                url,
                savedImage.getContentType()
        );

        return ResponseEntity.ok(response);
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
    public ResponseEntity<byte[]>  transformImage(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transformations to apply",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Transformations.class))
            )
            @RequestBody Transformations transformations) throws IOException {
        ImageOriginal dbImage = imgRepository.findById(id).orElseThrow();
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(dbImage.getData()));

        TransformImage transformer = new TransformImage(transformations, original);
        BufferedImage transformed = transformer.apply();

        String format = transformations.format() != null ? transformations.format() : "png";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(transformed, format, baos);
        byte[] newBytes = baos.toByteArray();

        if (Boolean.TRUE.equals(transformations.saved())) {
            dbImage.setData(newBytes);
            dbImage.setContentType("image/" + format);
            imgRepository.save(dbImage);
        }

        String transformedImgName = "";
        if (Boolean.TRUE.equals(transformations.copied())) {
            ImageOriginal transformedImg = new ImageOriginal();
            transformedImg.setName(dbImage.getName() + "_transformed");
            transformedImg.setContentType("image/" + format);
            transformedImg.setData(newBytes);
            imgRepository.save(transformedImg);
            imgRepository.save(dbImage);
            transformedImgName = transformedImg.getId().toString();
        }

        return ResponseEntity.ok()
                .header("Content-Type", "image/" + format)
                .header("X-Image-Id", transformedImgName)
                .body(newBytes);
    }

}
