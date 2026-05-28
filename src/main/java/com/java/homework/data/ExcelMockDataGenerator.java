package com.java.homework.data;

import com.alibaba.excel.EasyExcel;
import com.java.homework.dto.EmployeeDTO;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelMockDataGenerator {

    public void generateMockEmployeeExcel(String filePath, int totalRecords) {
        System.out.println("=== BẮT ĐẦU SINH FILE EXCEL MOCK DATA ===");
        long startTime = System.currentTimeMillis();

        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // Tạo bộ ghi file của EasyExcel gắn với class cấu trúc DTO
        var excelWriter = EasyExcel.write(filePath, EmployeeDTO.class).build();
        var writeSheet = EasyExcel.writerSheet("Danh sách nhân sự").build();

        int batchSize = 10000; // Cứ tạo 10,000 dòng dữ liệu giả thì ghi xuống đĩa 1 lần để tiết kiệm RAM
        List<EmployeeDTO> batchData = new ArrayList<>(batchSize);

        for (int i = 1; i <= totalRecords; i++) {
            EmployeeDTO mockEmp = new EmployeeDTO();
            mockEmp.setEmployeeCode("EMP" + String.format("%06d", i));
            mockEmp.setFullName("Nguyen Van " + i);
            mockEmp.setEmail("employee" + i + "@company.com");
            mockEmp.setPhoneNumber("0912" + String.format("%06d", i));

            // Chia phòng ban ngẫu nhiên cho có dữ liệu thực tế
            String[] departments = {"Phòng CNTT", "Phòng Nhân Sự", "Phòng Kinh Doanh", "Phòng Kế Toán"};
            mockEmp.setDepartment(departments[i % departments.length]);

            String[] positions = {"Nhân viên", "Trưởng nhóm", "Phó phòng", "Trưởng phòng"};
            mockEmp.setPosition(positions[i % positions.length]);

            batchData.add(mockEmp);

            // Khi list tạm đủ 10,000 dòng, ghi thẳng vào file Excel rồi clear list để giải phóng bộ nhớ
            if (i % batchSize == 0) {
                excelWriter.write(batchData, writeSheet);
                batchData.clear();
                System.out.println("Đã ghi thành công: " + i + " dòng...");
            }
        }

        // Ghi nốt những dòng còn dư lại (nếu có)
        if (!batchData.isEmpty()) {
            excelWriter.write(batchData, writeSheet);
            batchData.clear();
        }

        // Bắt buộc phải đóng bộ ghi để lưu file hoàn tất
        excelWriter.finish();

        long endTime = System.currentTimeMillis();
        System.out.println("=== ĐÃ SINH XONG FILE EXCEL TẠI: " + filePath);
        System.out.println("=== THỜI GIAN CHẠY: " + (endTime - startTime) + " ms ===");
    }
}
