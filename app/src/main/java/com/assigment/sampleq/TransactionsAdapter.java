package com.assigment.sampleq;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private final List<ExpenseSplitter.Transaction> transactions;

    public TransactionsAdapter(List<ExpenseSplitter.Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseSplitter.Transaction transaction = transactions.get(position);
        holder.transactionTextView.setText(transaction.toString());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView transactionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            transactionTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
