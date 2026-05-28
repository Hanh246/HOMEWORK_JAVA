package com.java.homework.controller;

import com.java.homework.data.ExcelMockDataGenerator;
import com.java.homework.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ExcelMockDataGenerator mockDataGenerator;

    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            employeeService.importExcelData(file.getInputStream());
            return ResponseEntity.ok("File đang được xử lý ngầm (Asynchronous) bằng Multi-thread!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi xử lý file: " + e.getMessage());
        }
    }

    @PostMapping("/generate-mock")
    public ResponseEntity<String> generateMockData(
            @RequestParam(value = "total", defaultValue = "100000") int totalRecords) {
        try {
            String filePath = "H:/Season_9/Thread/employee_mock_data.xlsx";

            mockDataGenerator.generateMockEmployeeExcel(filePath, totalRecords);

            return ResponseEntity.ok(String.format(
                    "Đã sinh thành công file Excel mock gồm %d dòng tại đường dẫn: %s",
                    totalRecords, filePath
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi sinh file mock data: " + e.getMessage());
        }
    }
}
