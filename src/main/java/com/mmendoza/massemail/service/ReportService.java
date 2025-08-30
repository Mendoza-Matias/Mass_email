package com.mmendoza.massemail.service;


import org.springframework.web.multipart.MultipartFile;

public interface ReportService {
    void readReport(MultipartFile file);
}
