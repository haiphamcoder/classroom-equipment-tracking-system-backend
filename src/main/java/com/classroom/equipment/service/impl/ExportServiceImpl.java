package com.classroom.equipment.service.impl;

import com.classroom.equipment.config.ApiException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;
import com.opencsv.CSVWriter;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

import com.classroom.equipment.service.ExportService;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {
    
    @Override
    public <T> ResponseEntity<Resource> exportToFile(List<T> data, String filename, String format) {
        Resource resource = switch (format.toLowerCase()) {
            case "excel" -> exportToExcel(data);
            case "csv" -> exportToCsv(data);
            default -> throw new ApiException("Unsupported export format: " + format);
        };
        
        String extension = format.equalsIgnoreCase("excel") ? "xlsx" : format.toLowerCase();
        String fullFilename = String.format("%s_%s.%s", 
            filename,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss")),
            extension);
            
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fullFilename + "\"")
            .header(HttpHeaders.CONTENT_TYPE, getContentType(format))
            .body(resource);
    }
    
    private <T> Resource exportToExcel(List<T> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            
            if (!data.isEmpty()) {
                Field[] fields = data.getFirst().getClass().getDeclaredFields();
                
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < fields.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(formatFieldName(fields[i].getName()));
                }
                
                int rowNum = 1;
                for (T item : data) {
                    Row row = sheet.createRow(rowNum++);
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        Cell cell = row.createCell(i);
                        setCellValue(cell, fields[i].get(item));
                    }
                }
                
                for (int i = 0; i < fields.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
            
        } catch (Exception e) {
            throw new ApiException("Failed to export to Excel: " + e.getMessage());
        }
    }
    
    private <T> Resource exportToCsv(List<T> data) {
        try {
            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);
            
            if (!data.isEmpty()) {
                Field[] fields = data.getFirst().getClass().getDeclaredFields();
                
                String[] headers = Arrays.stream(fields)
                    .map(field -> formatFieldName(field.getName()))
                    .toArray(String[]::new);
                csvWriter.writeNext(headers);
                
                for (T item : data) {
                    String[] rowData = new String[fields.length];
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        rowData[i] = formatFieldValue(fields[i].get(item));
                    }
                    csvWriter.writeNext(rowData);
                }
            }
            
            return new ByteArrayResource(stringWriter.toString().getBytes());
            
        } catch (Exception e) {
            throw new ApiException("Failed to export to CSV: " + e.getMessage());
        }
    }
    
    private String formatFieldName(String fieldName) {
        return Arrays.stream(fieldName.split("(?=\\p{Upper})"))
            .map(String::toLowerCase)
            .map(StringUtils::capitalize)
            .collect(Collectors.joining(" "));
    }
    
    private String formatFieldValue(Object value) {
        if (value == null) return "-";
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return value.toString();
    }
    
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("-");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            cell.setCellValue(value.toString());
        }
    }
    
    private String getContentType(String format) {
        return switch (format.toLowerCase()) {
            case "excel" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "csv" -> "text/csv";
            default -> "application/octet-stream";
        };
    }
} 