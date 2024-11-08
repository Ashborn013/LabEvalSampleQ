package com.assigment.sampleq;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddExpense extends AppCompatActivity {
    private EditText expenseTitleEditText;
    private EditText expenseAmountEditText;
    private Button addParticipantButton;
    private Button saveExpenseButton;
    private LinearLayout participantsContainer;

    private List<View> participantViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);

        expenseTitleEditText = findViewById(R.id.expenseTitleEditText);
        expenseAmountEditText = findViewById(R.id.expenseAmountEditText);
        addParticipantButton = findViewById(R.id.addParticipantButton);
        saveExpenseButton = findViewById(R.id.saveExpenseButton);
        participantsContainer = findViewById(R.id.participantsContainer);


        addParticipantButton.setOnClickListener(v -> addParticipantField());

        saveExpenseButton.setOnClickListener(v -> saveExpense());

    }

    private  void addParticipantField(){
        LinearLayout participantLayout = new LinearLayout(this);
//        participantLayout.setOrientation(LinearLayout.HORIZONTAL);
//        participantLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Name input
        EditText nameEditText = new EditText(this);
        nameEditText.setHint("Name");
        nameEditText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        participantLayout.addView(nameEditText);

        // Contribution input
        EditText contributionEditText = new EditText(this);
        contributionEditText.setHint("Contribution");
        contributionEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        contributionEditText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        participantLayout.addView(contributionEditText);

        // Add the participant layout to the container
        participantsContainer.addView(participantLayout);
        participantViews.add(participantLayout);

        calculateEqualContributions();

    }

    private void calculateEqualContributions() {
        // Get the total amount from the input field
        String amountStr = expenseAmountEditText.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr) || participantViews.isEmpty()) {
            return;
        }

        double totalAmount;
        try {
            totalAmount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate equal contribution
        double equalShare = totalAmount / participantViews.size();

        // Set each participant's contribution field to the equal share
        for (View view : participantViews) {
            LinearLayout participantLayout = (LinearLayout) view;
            EditText contributionEditText = (EditText) participantLayout.getChildAt(1);
            contributionEditText.setText(String.valueOf(equalShare));
        }
    }
    private  void saveExpense(){
        String title = expenseTitleEditText.getText().toString().trim();
        String amountStr = expenseAmountEditText.getText().toString().trim();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please enter title and amount", Toast.LENGTH_SHORT).show();
            return;
        }
        double totalAmount;
        try {
            totalAmount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> participantNames = new ArrayList<>();
        Map<String, Double> contributions = new HashMap<>();

        double equalShare = totalAmount / participantViews.size();
        for (View view : participantViews) {
            LinearLayout participantLayout = (LinearLayout) view;
            EditText nameEditText = (EditText) participantLayout.getChildAt(0);
            EditText contributionEditText = (EditText) participantLayout.getChildAt(1);

            String name = nameEditText.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please enter all participant names", Toast.LENGTH_SHORT).show();
                return;
            }

            double contribution;
            String contributionStr = contributionEditText.getText().toString().trim();
            if (TextUtils.isEmpty(contributionStr)) {
                contribution = equalShare; // Default to equal share
                contributionEditText.setText(String.valueOf(equalShare));
            } else {
                contribution = Double.parseDouble(contributionStr);
            }

            participantNames.add(name);
            contributions.put(name, contribution);
        }
        Intent intent = new Intent(AddExpense.this, Results.class);
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("transactions", (Serializable) contributions);
        startActivity(intent);



        // Here, you could save the expense data to a database or further process it
        Toast.makeText(this, "Expense saved with split!", Toast.LENGTH_LONG).show();
    }


}
