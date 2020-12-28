package com.example.mybudget;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatisticsFragment extends Fragment {

    private FirebaseFirestore db;

    private List<String> dates;
    private List<Double> amounts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dates = new ArrayList<>();
        amounts = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Line chart for balance
        BalanceLineChart balanceLineChart = new BalanceLineChart();
        // Pie chart for categories
        CategoryPieChart categoryPieChart = new CategoryPieChart();

        /* Firestore Database */
        db = FirebaseFirestore.getInstance();

        /* Get current user */
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        DocumentReference userRef = db.document("users/"+uid);
        CollectionReference transactionRef = db.collection("transactions");
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    Map<String, String> categoryNameMap = new HashMap<>();
                    Map<String, String> categoryColorMap = new HashMap<>();
                    Map<String, Double> categorySum = new TreeMap<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            categoryNameMap.put(document.getReference().getId(), (String) document.get("name"));
                            categoryColorMap.put((String) document.get("name"), (String) document.get("color"));
                        }
                        transactionRef.whereEqualTo("UID", userRef).orderBy("date", Query.Direction.ASCENDING).get().addOnCompleteListener(t -> {
                            if (t.isSuccessful()) {
                                double cumSum = 0;
                                for (QueryDocumentSnapshot document : t.getResult()) {
                                    Double amount = Double.parseDouble(document.get("amount").toString());
                                    cumSum += amount;

                                    if(amount < 0) {
                                        String categoryName = categoryNameMap.get(((DocumentReference) document.get("category")).getId());
                                        categorySum.put(categoryName, categorySum.getOrDefault(categoryName, 0.0) - amount);
                                    }

                                    Timestamp timestamp = (Timestamp) document.get("date");
                                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp.toDate());

                                    dates.add(date);
                                    amounts.add(cumSum);
                                }
                                balanceLineChart.render(view,R.id.balanceChartStatistics,dates,amounts);
                                categoryPieChart.render(view,R.id.categoryChart, categorySum, categoryColorMap);
                            }
                        });
                    }
                });

        return view;
    }
}
