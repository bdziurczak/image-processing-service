package com.example.imageprocessingservice.TableModels;

import com.example.imageprocessingservice.TableModels.Superclasses.ImageBase;
import jakarta.persistence.*;

@Entity
@Table(name = "transformed")
public class ImageTransformed extends ImageBase {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_id", nullable = false)
    private ImageOriginal imageOriginal;

}
