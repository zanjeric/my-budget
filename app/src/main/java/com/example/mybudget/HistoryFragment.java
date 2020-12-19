package com.example.mybudget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private static final int NUM_PAGES = 3;
    private static final String TAG = "History/transactions";
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    private FirebaseFirestore db;
    private List<Transaction> transactions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), this.getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        /* Firestore Database */
        db = FirebaseFirestore.getInstance();

        /* Get current user */
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        transactions = new ArrayList<>();

        DocumentReference userRef = db.document("users/"+uid);
        CollectionReference transactionRef = db.collection("transactions");
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    Map<String, String> categories = new HashMap<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            categories.put(document.getReference().getId(), (String) document.get("name"));
                        }
                        transactionRef.whereEqualTo("UID", userRef).get().addOnCompleteListener(t -> {
                            for (QueryDocumentSnapshot document : t.getResult()) {
                                String categoryName = categories.get(((DocumentReference) document.get("category")).getId());

                                Log.d("Transaction.category", "" + categoryName);
                                Timestamp timestamp = (Timestamp) document.get("date");
                                String date = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(timestamp.toDate());

                                Log.d("Transaction.date", date);
                                int amount = Integer.parseInt(document.get("amount").toString());

                                Log.d("Transaction.amount", String.valueOf(amount));
                                transactions.add(new Transaction(uid,""+categoryName,date,"",amount));
                            }
                        });
                    }
                });

        return view;
    }


    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm, Lifecycle lc) {
            super(fm, lc);
        }

        @Override
        public Fragment createFragment(int position) {
            return new HistorySlideFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}
