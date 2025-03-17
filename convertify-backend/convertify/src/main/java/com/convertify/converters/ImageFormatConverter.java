package com.convertify.converters;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFormatConverter {

    /**
     * Converts an image from one format to another.
     *
     * @param inputPath  Path of the source image file.
     * @param outputPath Path for the converted image.
     * @param formatName Target format (e.g., "png", "jpg").
     * @throws IOException If reading or writing fails.
     */
    public static void convert(String inputPath, String outputPath, String formatName) throws IOException {
        BufferedImage image = ImageIO.read(new File(inputPath));
        ImageIO.write(image, formatName, new File(outputPath));
        System.out.println("Image conversion complete: " + outputPath);
    }
}
