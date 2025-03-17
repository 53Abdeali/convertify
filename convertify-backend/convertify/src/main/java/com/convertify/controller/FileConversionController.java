package com.convertify.controller;

import com.convertify.converters.CsvExcelConverter;
import com.convertify.converters.ImageFormatConverter;
import com.convertify.converters.ImageToPDFConverter;
import com.convertify.converters.TextToPDFConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/convert")
@CrossOrigin(origins = "http://localhost:3000")
public class FileConversionController {

    // Use an absolute path for uploads based on the working directory.
    private final String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    // Ensure the upload directory exists.
    public FileConversionController() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Helper method to create JSON responses with a download URL.
    // Here we use our download endpoint: /api/download/{fileName}
    private ResponseEntity<Map<String, String>> createResponse(String message, String fileName) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        // Construct the download URL that forces a file download.
        String fileUrl = "http://localhost:8080/api/download/" + fileName;
        response.put("filePath", fileUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/image-format")
    public ResponseEntity<Map<String, String>> convertImageFormat(@RequestParam("file") MultipartFile file,
                                                                   @RequestParam("targetFormat") String targetFormat) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid file name"));
            }
            File uploadedFile = new File(uploadDir + originalName);
            file.transferTo(uploadedFile);

            String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
            String outputFileName = baseName + "." + targetFormat;
            String outputPath = uploadDir + outputFileName;

            ImageFormatConverter.convert(uploadedFile.getAbsolutePath(), outputPath, targetFormat);
            return createResponse("Image converted successfully", outputFileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error converting image format: " + e.getMessage()));
        }
    }

    @PostMapping("/image-to-pdf")
    public ResponseEntity<Map<String, String>> convertImageToPdf(@RequestParam("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid file name"));
            }
            File uploadedFile = new File(uploadDir + originalName);
            file.transferTo(uploadedFile);

            String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
            String outputFileName = baseName + ".pdf";
            String pdfPath = uploadDir + outputFileName;

            ImageToPDFConverter.convert(uploadedFile.getAbsolutePath(), pdfPath);
            return createResponse("Image converted to PDF", outputFileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error converting image to PDF: " + e.getMessage()));
        }
    }

    @PostMapping("/text-to-pdf")
    public ResponseEntity<Map<String, String>> convertTextToPdf(@RequestParam("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid file name"));
            }
            File uploadedFile = new File(uploadDir + originalName);
            file.transferTo(uploadedFile);

            String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
            String outputFileName = baseName + ".pdf";
            String pdfPath = uploadDir + outputFileName;

            TextToPDFConverter.convert(uploadedFile.getAbsolutePath(), pdfPath);
            return createResponse("Text converted to PDF", outputFileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error converting text to PDF: " + e.getMessage()));
        }
    }

    @PostMapping("/csv-to-excel")
    public ResponseEntity<Map<String, String>> convertCsvToExcel(@RequestParam("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid file name"));
            }
            File uploadedFile = new File(uploadDir + originalName);
            file.transferTo(uploadedFile);

            String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
            String outputFileName = baseName + ".xlsx";
            String excelPath = uploadDir + outputFileName;

            CsvExcelConverter.csvToExcel(uploadedFile.getAbsolutePath(), excelPath);
            return createResponse("CSV converted to Excel", outputFileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error converting CSV to Excel: " + e.getMessage()));
        }
    }
}
