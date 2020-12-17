package com.example.mybudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    Context context;
    ArrayList<Transaction> transactions;

    public TransactionAdapter(Context c, ArrayList<Transaction> t) {
        context = c;
        transactions = t;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.transactionCategory.setText(transactions.get(position).getCategory());
        holder.transactionDate.setText(transactions.get(position).getDate());
        holder.transactionAmount.setText(transactions.get(position).getAmount()+"€");
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView transactionCategory, transactionDate, transactionAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionCategory = (TextView) itemView.findViewById(R.id.categoryName);
            transactionDate = (TextView) itemView.findViewById(R.id.transactionDate);
            transactionAmount = (TextView) itemView.findViewById(R.id.transactionAmount);
        }
    }
}
