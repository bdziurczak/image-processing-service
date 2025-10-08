package com.example.imageprocessingservice.repositories;

import com.example.imageprocessingservice.TableModels.ImageTransformed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ImageTransformedRepository extends JpaRepository<ImageTransformed, Long> {
    List<ImageTransformed> findByImageOriginal_Id(Long originalId);
}


