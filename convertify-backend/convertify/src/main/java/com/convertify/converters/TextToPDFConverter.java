package com.convertify.converters;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextToPDFConverter {

    /**
     * Converts a text file to a PDF.
     *
     * @param txtPath Path of the text file.
     * @param pdfPath Output path for the PDF.
     * @throws IOException If the conversion fails.
     */
    public static void convert(String txtPath, String pdfPath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page);
             BufferedReader reader = new BufferedReader(new FileReader(txtPath))) {

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);

            String line;
            while ((line = reader.readLine()) != null) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
            }
            contentStream.endText();
        }

        document.save(pdfPath);
        document.close();
        System.out.println("Text to PDF conversion complete: " + pdfPath);
    }
}
