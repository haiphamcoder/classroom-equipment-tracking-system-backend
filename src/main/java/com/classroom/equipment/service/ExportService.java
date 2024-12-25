package com.classroom.equipment.service;

import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import java.util.List;

public interface ExportService {
    <T> ResponseEntity<Resource> exportToFile(List<T> data, String filename, String format);
} 