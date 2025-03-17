package com.convertify.converters;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

public class ImageToPDFConverter {

    /**
     * Converts an image file to a PDF.
     *
     * @param imagePath Path of the image file.
     * @param pdfPath   Output path for the PDF.
     * @throws IOException If the conversion fails.
     */
    public static void convert(String imagePath, String pdfPath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Adjust the coordinates and size as needed
            contentStream.drawImage(image, 50, 400, 500, 300);
        }
        document.save(pdfPath);
        document.close();
        System.out.println("Image to PDF conversion complete: " + pdfPath);
    }
}
