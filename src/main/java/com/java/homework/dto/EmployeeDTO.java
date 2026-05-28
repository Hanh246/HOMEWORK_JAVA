package com.java.homework.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EmployeeDTO {
    @ExcelProperty("Mã Nhân Viên")
    private String employeeCode;

    @ExcelProperty("Họ Và Tên")
    private String fullName;

    @ExcelProperty("Email")
    private String email;

    @ExcelProperty("Số Điện Thoại")
    private String phoneNumber;

    @ExcelProperty("Phòng Ban")
    private String department;

    @ExcelProperty("Chức Vụ")
    private String position;
}
