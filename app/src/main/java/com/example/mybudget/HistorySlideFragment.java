package com.example.mybudget;


import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistorySlideFragment extends Fragment {

    private Parcelable[] transactions;
    private String month;

    private RecyclerView transactionsRecyclerView;
    private TransactionAdapter transactionAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transactions = getArguments().getParcelableArray("transactions");
        month = getArguments().getString("month");
        Log.d("HistorySlide.month", month);
//        for (Parcelable t :
//                transactions) {
//            Log.d("Transaction.Parcel.info", ((TransactionParcel) t).getDate());
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_slide, container, false);

        TextView monthText = view.findViewById(R.id.monthTitle);
        monthText.setText(month);

        /* Transaction RecyclerView */
        transactionsRecyclerView = view.findViewById(R.id.perMonthTransactions);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsRecyclerView.setNestedScrollingEnabled(false);

        ArrayList<Transaction> perMonth = new ArrayList<>();
        for (Parcelable t :
                transactions) {
            TransactionParcel tp = (TransactionParcel) t;
            perMonth.add(new Transaction("", tp.getCategory(), tp.getDate(), "", tp.getAmount()));
        }

        transactionAdapter = new TransactionAdapter(getActivity(), perMonth);
        transactionAdapter.notifyDataSetChanged();
        transactionsRecyclerView.setAdapter(transactionAdapter);

        return view;
    }


}