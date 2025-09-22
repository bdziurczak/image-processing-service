package com.example.imageprocessingservice.utils;

import com.example.imageprocessingservice.DTOs.Transformations;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

public class TransformImage {
    private final Transformations transformations;
    private BufferedImage image;
    private final List<String> applied = new ArrayList<>();

    public TransformImage(Transformations transformations, BufferedImage image) {
        this.transformations = transformations;
        this.image = image;
    }

    private <T> void applyTransformation(T value, Consumer<T> action) {
        Optional.ofNullable(value).ifPresent(action);
    }

    public BufferedImage apply() {
        applyTransformation(transformations.resize(), this::resize);
        applyTransformation(transformations.crop(), this::crop);
        applyTransformation(transformations.rotate(), this::rotate);
        applyTransformation(transformations.filters(), this::filters);
        return image;
    }

    public List<String> getAppliedTransformations() {
        return applied;
    }

    private void resize(Transformations.Resize resize) {
        if (resize.width() != null && resize.height() != null) {
            Image tmp = image.getScaledInstance(resize.width(), resize.height(), java.awt.Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(resize.width(), resize.height(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            image = resized;
            applied.add("Resized to " + resize.width() + "x" + resize.height());
        }
    }

    private void crop(Transformations.Crop crop) {
        if (crop.width() != null && crop.height() != null && crop.x() != null && crop.y() != null) {
            image = image.getSubimage(crop.x(), crop.y(), crop.width(), crop.height());
            applied.add("Cropped to " + crop.width() + "x" + crop.height() + " at (" + crop.x() + "," + crop.y() + ")");
        }
    }

    private void rotate(Integer angle) {
        if (angle != null && angle != 0) {
            double rads = Math.toRadians(angle);
            double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
            int w = image.getWidth();
            int h = image.getHeight();
            int newW = (int) Math.floor(w * cos + h * sin);
            int newH = (int) Math.floor(h * cos + w * sin);
            BufferedImage rotated = new BufferedImage(newW, newH, image.getType());
            Graphics2D g2d = rotated.createGraphics();
            g2d.translate((newW - w) / 2, (newH - h) / 2);
            g2d.rotate(rads, w / 2.0, h / 2.0);
            g2d.drawRenderedImage(image, null);
            g2d.dispose();
            image = rotated;
            applied.add("Rotated " + angle + " degrees");
        }
    }

    private void filters(Transformations.Filters filters) {
        if (filters.grayscale() != null && filters.grayscale()) {
            image = applyGrayscale(image);
            applied.add("Applied grayscale");
        }
        if (filters.sepia() != null && filters.sepia()) {
            image = applySepia(image);
            applied.add("Applied sepia");
        }
    }

    private BufferedImage applyGrayscale(BufferedImage img) {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int gray = (int) (0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue());
                result.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        return result;
    }

    private BufferedImage applySepia(BufferedImage img) {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int tr = (int) (0.393 * c.getRed() + 0.769 * c.getGreen() + 0.189 * c.getBlue());
                int tg = (int) (0.349 * c.getRed() + 0.686 * c.getGreen() + 0.168 * c.getBlue());
                int tb = (int) (0.272 * c.getRed() + 0.534 * c.getGreen() + 0.131 * c.getBlue());
                tr = Math.min(255, tr);
                tg = Math.min(255, tg);
                tb = Math.min(255, tb);
                result.setRGB(x, y, new Color(tr, tg, tb).getRGB());
            }
        }
        return result;
    }
}