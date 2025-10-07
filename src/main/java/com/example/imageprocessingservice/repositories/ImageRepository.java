package com.example.imageprocessingservice.repositories;
import com.example.imageprocessingservice.TableModels.ImageOriginal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageOriginal, Long> {
}
