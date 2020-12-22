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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView transactionsRecyclerView;
    private ArrayList<Transaction> recentTransactions;
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

        /* Transaction RecyclerView */
        transactionsRecyclerView = view.findViewById(R.id.transactions);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsRecyclerView.setNestedScrollingEnabled(false);


        recentTransactions = new ArrayList<Transaction>();
        CollectionReference transactionRef = db.collection("transactions");
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    Map<String, String> categories = new HashMap<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            categories.put(document.getReference().getId(), (String) document.get("name"));
                        }
                        transactionRef.whereEqualTo("UID", userRef).orderBy("date", Query.Direction.DESCENDING).limit(4).get().addOnCompleteListener(t -> {
                            if (t.isSuccessful()) {
                                for (QueryDocumentSnapshot document : t.getResult()) {
                                    String categoryName = categories.get(((DocumentReference) document.get("category")).getId());
                                    Log.d("Transaction.category", "" + categoryName);

                                    Timestamp timestamp = (Timestamp) document.get("date");
                                    String date = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(timestamp.toDate());
                                    Log.d("Transaction.date", date);

                                    int amount = Integer.parseInt(document.get("amount").toString());
                                    Log.d("Transaction.amount", String.valueOf(amount));

                                    recentTransactions.add(new Transaction(uid, categoryName, date, categoryName, amount));
                                }
                                transactionAdapter = new TransactionAdapter(getActivity(), recentTransactions);
                                transactionAdapter.notifyDataSetChanged();
                                transactionsRecyclerView.setAdapter(transactionAdapter);
                            }
                        });
                    }
                });

        // Line chart for balance
        BalanceLineChart balanceLineChart = new BalanceLineChart();
        List<String> dates = new ArrayList<>();
        List<Double> amounts = new ArrayList<>();

        db.collection("transactions")
                .whereEqualTo("UID", userRef)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        double cumSum = 0;
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            Double amount = Double.parseDouble(document.get("amount").toString());
                            cumSum += amount;

                            Timestamp timestamp = (Timestamp) document.get("date");
                            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp.toDate());

                            dates.add(date);
                            amounts.add(cumSum);
                        }
                        balanceLineChart.render(view,R.id.balanceChart,dates,amounts);
                    }
                });
        return view;
    }
}
