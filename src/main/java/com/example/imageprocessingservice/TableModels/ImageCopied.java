package com.example.imageprocessingservice.TableModels;

import com.example.imageprocessingservice.TableModels.Superclasses.ImageBase;
import jakarta.persistence.*;

@Entity
@Table(name = "copied")
public class ImageCopied extends ImageBase {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_id", nullable = false)
    private ImageOriginal imageOriginal;

    public ImageOriginal getImageOriginal() {
        return imageOriginal;
    }

    public void setImageOriginal(ImageOriginal imageOriginal) {
        this.imageOriginal = imageOriginal;
    }
}
