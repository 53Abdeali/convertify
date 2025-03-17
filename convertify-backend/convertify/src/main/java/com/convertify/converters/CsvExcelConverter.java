package com.convertify.converters;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class CsvExcelConverter {

    /**
     * Converts a CSV file to an Excel XLSX file.
     *
     * @param csvPath   Path of the CSV file.
     * @param excelPath Output path for the XLSX file.
     * @throws IOException If the conversion fails.
     */
    public static void csvToExcel(String csvPath, String excelPath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath));
                Workbook workbook = new XSSFWorkbook();
                FileOutputStream fileOut = new FileOutputStream(excelPath)) {

            Sheet sheet = workbook.createSheet("Sheet1");
            String line;
            int rowNum = 0;
            while ((line = br.readLine()) != null) {
                Row row = sheet.createRow(rowNum++);
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    row.createCell(i).setCellValue(values[i]);
                }
            }
            workbook.write(fileOut);
            System.out.println("CSV to Excel conversion complete: " + excelPath);
        }
    }

    // You can implement excelToCsv here if needed.
}
