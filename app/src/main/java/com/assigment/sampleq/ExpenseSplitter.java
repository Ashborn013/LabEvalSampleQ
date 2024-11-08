package com.assigment.sampleq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseSplitter {

    public static class Transaction {
        String from;
        String to;
        double amount;

        public Transaction(String from, String to, double amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return from + " owes " + to + " $" + amount;
        }
    }

    public static List<Transaction> calculateSettlements(Map<String, Double> contributions, double totalAmount) {
        // Calculate per-person share
        double equalShare = totalAmount / contributions.size();

        // Calculate each participant's balance (positive: owed, negative: owes)
        Map<String, Double> balances = new HashMap<>();
        for (Map.Entry<String, Double> entry : contributions.entrySet()) {
            balances.put(entry.getKey(), entry.getValue() - equalShare);
        }

        // Separate into creditors and debtors
        List<String> creditors = new ArrayList<>();
        List<String> debtors = new ArrayList<>();
        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(entry.getKey());
            } else if (entry.getValue() < 0) {
                debtors.add(entry.getKey());
            }
        }

        List<Transaction> transactions = new ArrayList<>();
        int creditorIndex = 0;
        int debtorIndex = 0;

        // Settle debts with minimal transactions
        while (creditorIndex < creditors.size() && debtorIndex < debtors.size()) {
            String creditor = creditors.get(creditorIndex);
            String debtor = debtors.get(debtorIndex);
            double creditorBalance = balances.get(creditor);
            double debtorBalance = -balances.get(debtor);
            double amount = Math.min(creditorBalance, debtorBalance);

            transactions.add(new Transaction(debtor, creditor, amount));

            // Update balances
            balances.put(creditor, creditorBalance - amount);
            balances.put(debtor, debtorBalance - amount);

            // Move to next creditor or debtor if balance is settled
            if (balances.get(creditor) == 0) {
                creditorIndex++;
            }
            if (balances.get(debtor) == 0) {
                debtorIndex++;
            }
        }

        return transactions;
    }
}
