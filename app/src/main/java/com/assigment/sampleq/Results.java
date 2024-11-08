package com.assigment.sampleq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Results extends AppCompatActivity {

    private TextView resultSummary;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultSummary = findViewById(R.id.resultSummary);
        shareButton = findViewById(R.id.shareButton);

        // Retrieve transaction details passed from AddExpenseActivity
        Intent intent = getIntent();
        if (intent != null) {
            Serializable transactionData = intent.getSerializableExtra("transactions");
            if (transactionData != null) {
                displayResults((Map<String, Double>) transactionData);
            }
        }

        shareButton.setOnClickListener(v -> shareResults());
    }

    private void displayResults(Map<String, Double> contributions) {
        StringBuilder resultText = new StringBuilder();

        for (Map.Entry<String, Double> entry : contributions.entrySet()) {
            resultText.append(entry.getKey()).append(" contributed ").append(entry.getValue()).append("\n");
        }

        resultSummary.setText(resultText.toString());
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
