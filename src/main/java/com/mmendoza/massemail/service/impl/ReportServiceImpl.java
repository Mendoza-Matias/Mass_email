package com.mmendoza.massemail.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mmendoza.massemail.exception.ReadingReportException;
import com.mmendoza.massemail.model.User;
import com.mmendoza.massemail.service.ReportService;
import com.mmendoza.massemail.service.UserService;

@Service
public class ReportServiceImpl implements ReportService {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    public ReportServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void readReport(MultipartFile file) {
        logger.info("Reading report");
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            List<User> users = parseWorkbook(workbook);
            userService.saveManyUser(users);
        } catch (Exception e) {
            logger.error("Error reading report: " + e.getMessage());
            throw new ReadingReportException("Error reading report: " + e.getMessage());
        }
    }

    private List<User> parseWorkbook(Workbook workbook) {
        Set<User> users = new LinkedHashSet<>();
        DataFormatter formatter = new DataFormatter();

        workbook.forEach(sheet -> { // paginas
            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // Saltar encabezado

                String name = formatter.formatCellValue(row.getCell(0));
                String email = formatter.formatCellValue(row.getCell(1));
                String message = formatter.formatCellValue(row.getCell(2));

                if (name.isBlank() || email.isBlank() || message.isBlank()) {
                    continue; // Saltar filas con datos incompletos
                }

                User user = new User(name, email, message);

                users.add(user);
            }
        });
        return new ArrayList<>(users); // convertir a lista
    }
}
