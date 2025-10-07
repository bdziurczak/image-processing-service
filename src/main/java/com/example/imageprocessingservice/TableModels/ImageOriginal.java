package com.example.imageprocessingservice.TableModels;

import com.example.imageprocessingservice.TableModels.Superclasses.ImageBase;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "originals")
public class ImageOriginal extends ImageBase {
    @OneToMany(mappedBy = "original", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageTransformed> transformedImages =  new ArrayList<>();

    @OneToMany(mappedBy = "original", cascade = CascadeType.ALL)
    private List<ImageCopied> copiedImages = new ArrayList<>();
}
