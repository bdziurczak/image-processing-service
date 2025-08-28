package com.example.imageprocessingservice.utils;

import com.example.imageprocessingservice.DTOs.Transformations;
import com.example.imageprocessingservice.models.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TransformImage {
    private final Transformations transformations;
    private Image image;
    private final List<String> applied = new ArrayList<>();

    public TransformImage(Transformations transformations, Image image) {
        this.transformations = transformations;
        this.image = image;
    }
    private <T> void applyTransformation(T value, Consumer<T> action)  {
        Optional.ofNullable(value).ifPresent(action);
    }
    public Image apply() {
        applyTransformation(transformations.resize(), this::resize);
        applyTransformation(transformations.crop(), this::crop);
        applyTransformation(transformations.rotate(), this::rotate);
        applyTransformation(transformations.format(), this::changeFormat);
        applyTransformation(transformations.filters(), this::filters);

        return image;
    }
    public List<String> getAppliedTransformations() {
        return applied;
    }

    private void resize(Transformations.Resize resize) {
        Optional.ofNullable(resize.width())
                .flatMap(width -> Optional.ofNullable(resize.height())
                        .map(height -> new int[]{width, height}))
                .ifPresent(size -> {
                    int w = size[0], h = size[1];
                    // TODO: implement resize
                    applied.add("Resized to " + w + "x" + h);
                });
    }

    private void crop(Transformations.Crop crop) {
        if (crop.width() != null && crop.height() != null &&
                crop.x() != null && crop.y() != null) {
            // TODO: implement crop
            applied.add("Cropping to " + crop.width() + "x" + crop.height() +
                    " from (" + crop.x() + "," + crop.y() + ")");
        }
    }

    private void rotate(Integer angle) {
        // TODO: implement rotate
        applied.add("Rotating " + angle + " degrees");
    }

    private void changeFormat(String format) {
        // TODO: implement format change
        applied.add("Converting to format " + format);
    }

    private void filters(Transformations.Filters filters) {
        Optional.ofNullable(filters.grayscale())
                .filter(Boolean::booleanValue)
                .ifPresent(x -> applied.add("Applying grayscale"));

        Optional.ofNullable(filters.sepia())
                .filter(Boolean::booleanValue)
                .ifPresent(x -> applied.add("Applying sepia"));
    }
}