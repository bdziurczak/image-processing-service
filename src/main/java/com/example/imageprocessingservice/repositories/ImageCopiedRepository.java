package com.example.imageprocessingservice.repositories;

import com.example.imageprocessingservice.TableModels.ImageCopied;
import com.example.imageprocessingservice.TableModels.ImageOriginal;
import com.example.imageprocessingservice.TableModels.ImageTransformed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageCopiedRepository extends JpaRepository<ImageCopied, Long> {
    List<ImageTransformed> findByImageOriginal_Id(Long originalId);
}

