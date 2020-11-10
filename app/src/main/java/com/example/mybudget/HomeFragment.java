package com.example.mybudget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView transactionsRecyclerView;
    private ArrayList<Transaction> list;
    private TransactionAdapter transactionAdapter;
    private TextView balance, income, expense;
    private static final String TAG = "Fetch transactions";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        /* Firestore Database */
        db = FirebaseFirestore.getInstance();

        /* Get current user */
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        transactionsRecyclerView = view.findViewById(R.id.transactions);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        list = new ArrayList<Transaction>();
        list.add(new Transaction("3","Health","10.11.2020","hello",100));
        list.add(new Transaction("3","Food","12.34.3234","hello",200));
        list.add(new Transaction("3","Other","12.34.3234","hello",100));
        list.add(new Transaction("3","Transport","12.34.3234","hello",200));
        transactionAdapter = new TransactionAdapter(getActivity(), list);
        transactionAdapter.notifyDataSetChanged();
        transactionsRecyclerView.setAdapter(transactionAdapter);


        balance = view.findViewById(R.id.balanceValue);
        income = view.findViewById(R.id.incomeValue);
        expense = view.findViewById(R.id.expenseValue);

        // Get balance data from database

        DocumentReference userRef = db.document("users/"+uid);
        db.collection("transactions")
                .whereEqualTo("UID", userRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int sumBalance = 0;
                        int sumIncome = 0;
                        int sumExpense = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int amount = Integer.parseInt(document.get("amount").toString());
                                sumBalance+=amount;

                                if(amount >= 0) {
                                    sumIncome+=amount;
                                }
                                else {
                                    sumExpense+=amount;
                                }
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            balance.setText(sumBalance+"€");
                            income.setText(sumIncome+"€");
                            expense.setText(sumExpense+"€");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        Log.d("TAG", balance.getText().toString());

        return view;
    }
}
