package com.assigment.sampleq;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class Results extends AppCompatActivity {
    private RecyclerView transactionsRecyclerView;
    private TextView summaryTextView;
    private Button shareButton;
    private List<ExpenseSplitter.Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        summaryTextView = findViewById(R.id.summaryTextView);
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
        shareButton = findViewById(R.id.shareButton);

        // Get the transactions list from intent (assuming we pass it from previous activity)
        transactions = (List<ExpenseSplitter.Transaction>) getIntent().getSerializableExtra("transactions");

        // Set up RecyclerView
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TransactionsAdapter adapter = new TransactionsAdapter(transactions);
        transactionsRecyclerView.setAdapter(adapter);

        // Set up Share Button
        shareButton.setOnClickListener(v -> shareSummary());
    }
    private void shareSummary() {
        StringBuilder summaryBuilder = new StringBuilder();
        summaryBuilder.append("Expense Settlement Summary:\n");
        for (ExpenseSplitter.Transaction transaction : transactions) {
            summaryBuilder.append(transaction.toString()).append("\n");
        }

        // Create sharing intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, summaryBuilder.toString());
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

}
