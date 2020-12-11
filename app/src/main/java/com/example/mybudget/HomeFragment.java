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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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


        list = new ArrayList<Transaction>();
        //list.add(new Transaction("3","Health","10.11.2020","hello",100));

        db.collection("transactions")
                .whereEqualTo("UID", userRef)
                //.orderBy("category", Query.Direction.ASCENDING)
                .limit(4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String categoryName = "catName";

                                Timestamp timestamp = (Timestamp) document.get("date");
                                String date = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(timestamp.toDate());

                                int amount = Integer.parseInt(document.get("amount").toString());
                                list.add(new Transaction(uid,categoryName,date,"",amount));

                            }
                            transactionAdapter = new TransactionAdapter(getActivity(), list);
                            transactionAdapter.notifyDataSetChanged();
                            transactionsRecyclerView.setAdapter(transactionAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Line chart for balance
        BalanceLineChart balanceLineChart = new BalanceLineChart();
        List<String> dates = new ArrayList<>();
        dates.add("2020-01-29");
        dates.add("2020-04-11");
        dates.add("2020-06-25");
        dates.add("2020-09-05");
        dates.add("2020-11-02");

        List<Double> amounts = new ArrayList<>();
        amounts.add(300.2);
        amounts.add(100.2);
        amounts.add(500.2);
        amounts.add(900.2);
        amounts.add(330.2);

        balanceLineChart.render(view,R.id.balanceChart,dates,amounts);

        return view;
    }
}
