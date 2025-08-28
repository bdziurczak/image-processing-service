package com.example.imageprocessingservice.DTOs;

public record Transformations(
        Resize resize,
        Crop crop,
        Integer rotate,
        String format,
        Filters filters,
        Boolean saved
) {
    public record Resize(Integer width, Integer height) {}
    public record Crop(Integer width, Integer height, Integer x, Integer y) {}
    public record Filters(Boolean grayscale, Boolean sepia) {}
}