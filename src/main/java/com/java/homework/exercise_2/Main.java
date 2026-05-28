package com.java.homework.exercise_2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
    List<Account> accounts = new ArrayList<>();
    for (int i = 1; i <= 100; i++) {
        accounts.add(new Account(i, 10000));
    }

    ExecutorService executor = Executors.newFixedThreadPool(20);

    System.out.println("=== BẮT ĐẦU THỰC HIỆN 1000 GIAO DỊCH ===");

    // 3. Đẩy 1000 công việc chuyển tiền vào Thread Pool
    for (int i = 0; i < 1000; i++) {
        executor.execute(new TransferTask(accounts));
    }

    executor.shutdown();

    try {
        if (executor.awaitTermination(5, TimeUnit.MINUTES)) {

            int totalBalance = 0;
            for (Account acc : accounts) {
                totalBalance += acc.getBalance();
                System.out.println(acc.getId() +  ":" + acc.getBalance());
            }

            System.out.println("\n====================================");
            System.out.println("KẾT QUẢ KIỂM TRA HỆ THỐNG:");
            System.out.println("Tổng số tiền cuối cùng: " + totalBalance + "$");

            if (totalBalance == 1000000) {
                System.out.println("TRẠNG THÁI: THÀNH CÔNG (Tổng tiền được bảo toàn chuẩn xác!)");
            } else {
                System.out.println("TRẠNG THÁI: THẤT BẠI (Có lỗi xung đột dữ liệu xảy ra!)");
            }
            System.out.println("====================================");
        }
    } catch (InterruptedException e) {
        System.out.println("Quá trình đợi bị ngắt quãng.");
        e.printStackTrace();
    }
}
}
