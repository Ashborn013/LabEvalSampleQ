package com.assigment.sampleq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results extends AppCompatActivity {

    private TextView resultSummary;
    private Button shareButton;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultSummary = findViewById(R.id.resultSummary);
        shareButton = findViewById(R.id.shareButton);

        // Retrieve transaction details and total amount from AddExpense
        Intent intent = getIntent();
        if (intent != null) {
            totalAmount = intent.getDoubleExtra("totalAmount", 0.0);
            Serializable transactionData = intent.getSerializableExtra("transactions");
            if (transactionData instanceof Map) {
                displayResults((Map<String, Double>) transactionData);
            }
        }

        shareButton.setOnClickListener(v -> shareResults());
    }

    private void displayResults(Map<String, Double> contributions) {
        StringBuilder resultText = new StringBuilder();

        // Calculate the net balance (who owes or is owed)
        Map<String, Double> balances = calculateBalances(contributions);
        resultText.append("Total Amount: ").append(totalAmount).append("\n\n");

        resultText.append("Balances:\n");
        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            String balanceDescription = entry.getValue() >= 0 ?
                    entry.getKey() + " is owed $" + entry.getValue() :
                    entry.getKey() + " owes $" + (-entry.getValue());
            resultText.append(balanceDescription).append("\n");
        }

        resultText.append("\nTransactions:\n");
        List<String> transactions = minimizeTransactions(balances);
        for (String transaction : transactions) {
            resultText.append(transaction).append("\n");
        }

        resultSummary.setText(resultText.toString());
    }

    private Map<String, Double> calculateBalances(Map<String, Double> contributions) {
        Map<String, Double> balances = new HashMap<>();
        double totalContributions = 0.0;

        // Calculate the total contributions
        for (double contribution : contributions.values()) {
            totalContributions += contribution;
        }

        double individualShare = totalAmount / contributions.size();
        double excessAmount = totalContributions - totalAmount;

        for (Map.Entry<String, Double> entry : contributions.entrySet()) {
            double contribution = entry.getValue();
            double excessShare = (excessAmount > 0) ? (contribution / totalContributions) * excessAmount : 0;
            balances.put(entry.getKey(), contribution - individualShare - excessShare);
        }

        return balances;
    }

    private List<String> minimizeTransactions(Map<String, Double> balances) {
        List<String> transactions = new ArrayList<>();
        List<Map.Entry<String, Double>> creditors = new ArrayList<>();
        List<Map.Entry<String, Double>> debtors = new ArrayList<>();

        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(entry);
            } else if (entry.getValue() < 0) {
                debtors.add(entry);
            }
        }

        int i = 0, j = 0;
        while (i < creditors.size() && j < debtors.size()) {
            Map.Entry<String, Double> creditor = creditors.get(i);
            Map.Entry<String, Double> debtor = debtors.get(j);

            double creditAmount = creditor.getValue();
            double debtAmount = -debtor.getValue();
            double settlementAmount = Math.min(creditAmount, debtAmount);

            transactions.add(debtor.getKey() + " owes " + creditor.getKey() + " $" + String.format("%.2f", settlementAmount));

            creditors.set(i, Map.entry(creditor.getKey(), creditAmount - settlementAmount));
            debtors.set(j, Map.entry(debtor.getKey(), -debtAmount + settlementAmount));

            if (creditors.get(i).getValue() == 0) i++;
            if (debtors.get(j).getValue() == 0) j++;
        }

        return transactions;
    }

    private void shareResults() {
        String summary = resultSummary.getText().toString();

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, summary);
        shareIntent.setType("text/plain");

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}