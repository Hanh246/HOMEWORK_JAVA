package com.java.homework.service;

import com.alibaba.excel.EasyExcel;
import com.java.homework.dto.EmployeeDTO;
import com.java.homework.entity.Employee;
import com.java.homework.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    @Qualifier("excelExcelExecutor")
    private Executor executor;


    public void importExcelData(InputStream inputStream) {
        log.info("[EmployeeService] Bắt đầu khởi tạo quy trình đọc Excel Stream...");
        EasyExcel.read(inputStream, EmployeeDTO.class, new EmployeeExcelListener(executor, this))
                .sheet()
                .doRead();
    }

    @Transactional
    public void saveToDatabase(List<EmployeeDTO> list, int batchId) {
        String threadName = Thread.currentThread().getName();

        log.info("[DB Writer] -> [{}] Bắt đầu xử lý lưu mẻ số {} (gồm {} bản ghi)...", threadName, batchId, list.size());
        long startTime = System.currentTimeMillis();

        List<Employee> entities = list.stream().map(this::convertToEntity).toList();

        employeeRepository.saveAll(entities);

        long endTime = System.currentTimeMillis();
        log.info("[DB Writer] <- [{}] THÀNH CÔNG lưu mẻ số {} vào DB. Thời gian xử lý: {} ms", threadName, batchId, (endTime - startTime));
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee entity = new Employee();

        entity.setEmployeeCode(dto.getEmployeeCode());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setDepartment(dto.getDepartment());
        entity.setPosition(dto.getPosition());

        return entity;
    }
}
