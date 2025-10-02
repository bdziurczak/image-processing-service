package com.example.imageprocessingservice.DTOs;

public class ImageResponse {
    private Long id;
    private String url;
    private String contentType;

    public ImageResponse(Long id, String url, String contentType) {
        this.id = id;
        this.url = url;
        this.contentType = contentType;
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public String getContentType() { return contentType; }
}
