package com.java.homework.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.java.homework.dto.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
public class EmployeeExcelListener extends AnalysisEventListener<EmployeeDTO> {

    private final Executor executor;
    private final EmployeeService employeeService;
    private List<EmployeeDTO> cachedDataList = new ArrayList<>(2000);
    private final int BATCH_COUNT = 2000;
    private int batchCounter = 0;

    public EmployeeExcelListener(Executor executor, EmployeeService employeeService) {
        this.executor = executor;
        this.employeeService = employeeService;
    }

    @Override
    public void invoke(EmployeeDTO data, AnalysisContext context) {
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            batchCounter++;

            List<EmployeeDTO> taskData = new ArrayList<>(cachedDataList);

            int currentBatch = batchCounter;
            log.info("[Excel Reader] Đã đọc đủ mẻ số {} ({} dòng). Đang đẩy sang ThreadPool...", currentBatch, BATCH_COUNT);

            executor.execute(() -> employeeService.saveToDatabase(taskData, currentBatch));
            cachedDataList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!cachedDataList.isEmpty()) {
            batchCounter++;

            List<EmployeeDTO> taskData = new ArrayList<>(cachedDataList);

            int currentBatch = batchCounter;
            log.info("[Excel Reader] Đã đọc xong toàn bộ file. Mẻ cuối cùng (số {}) có {} dòng. Đang đẩy nốt sang ThreadPool...", currentBatch, taskData.size());

            executor.execute(() -> employeeService.saveToDatabase(taskData, currentBatch));
            cachedDataList.clear();
        }
    }
}
