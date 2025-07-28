package com.example.imageprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ImageProcessingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ImageProcessingServiceApplication.class, args);
	}
}
