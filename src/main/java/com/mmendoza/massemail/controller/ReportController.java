package com.mmendoza.massemail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mmendoza.massemail.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Report Management", description = "Operations related to report management")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Upload a report", description = "Uploads a report file for processing")
    @ApiResponse(responseCode = "200", description = "Report uploaded successfully")
    @ApiResponse(responseCode = "400", description = "Invalid report file")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping()
    public ResponseEntity<Void> uploadReport(@RequestParam(name = "file") MultipartFile file) {
        reportService.readReport(file); 
        return ResponseEntity.ok().build();
    }
}
