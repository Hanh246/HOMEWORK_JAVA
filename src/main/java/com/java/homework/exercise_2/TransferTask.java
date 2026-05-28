package com.java.homework.exercise_2;

import java.util.List;
import java.util.Random;

public class TransferTask implements Runnable {
    private final List<Account> accounts;
    private final Random random = new Random();

    public TransferTask(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void run() {
        int fromIdx = random.nextInt(accounts.size());
        int toIdx = random.nextInt(accounts.size());

        while (fromIdx == toIdx) {
            toIdx = random.nextInt(accounts.size());
        }

        Account fromAccount = accounts.get(fromIdx);
        Account toAccount = accounts.get(toIdx);

        int amount = random.nextInt(491) + 10;

        Account firstLock = fromAccount.getId() < toAccount.getId() ? fromAccount : toAccount;
        Account secondLock = fromAccount.getId() < toAccount.getId() ? toAccount : fromAccount;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (fromAccount.withdraw(amount)) {
                    toAccount.deposit(amount);

                    StringBuilder log = new StringBuilder();
                    log.append("[").append(Thread.currentThread().getName()).append("]\n")
                            .append("Transfer SUCCESS\n")
                            .append("FROM Account-").append(fromAccount.getId()).append("\n")
                            .append("TO Account-").append(toAccount.getId()).append("\n")
                            .append("AMOUNT: ").append(amount).append("\n\n")
                            .append("Balances:\n")
                            .append("Account-").append(fromAccount.getId()).append(" = ").append(fromAccount.getBalance()).append("\n")
                            .append("Account-").append(toAccount.getId()).append(" = ").append(toAccount.getBalance()).append("\n")
                            .append("------------------------------------");
                    System.out.println(log);
                } else {
                    System.out.println("[" + Thread.currentThread().getName() + "] Transfer FAILED: Account-" + fromAccount.getId() + " không đủ tiền.");
                    System.out.println("------------------------------------");
                }
            }
        }
    }
}
