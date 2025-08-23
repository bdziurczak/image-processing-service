package com.example.imageprocessingservice.repositories;
import com.example.imageprocessingservice.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
