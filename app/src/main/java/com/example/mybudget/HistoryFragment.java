package com.example.mybudget;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private static final String TAG = "History/transactions";
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    private FirebaseFirestore db;
    private List<TransactionParcel> transactions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        viewPager = view.findViewById(R.id.viewPager);

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
                        transactionRef.whereEqualTo("UID", userRef).orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(t -> {
                            if (t.isSuccessful()) {
                                pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), this.getLifecycle());
                                int mCount = 0;
                                String prevMonth = "xx";

                                List<TransactionParcel> perMonth = new ArrayList<>();
                                for (QueryDocumentSnapshot document : t.getResult()) {
                                    String categoryName = categories.get(((DocumentReference) document.get("category")).getId());
                                    Log.d("Transaction.category", "" + categoryName);

                                    Timestamp timestamp = (Timestamp) document.get("date");
                                    String date = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(timestamp.toDate());
                                    Log.d("Transaction.date", date);

                                    int amount = Integer.parseInt(document.get("amount").toString());
                                    Log.d("Transaction.amount", String.valueOf(amount));

                                    TransactionParcel T = new TransactionParcel("" + categoryName, date, amount);

                                    if(!date.startsWith(prevMonth)) {
                                        if(!prevMonth.equals("xx"))
                                            ((ScreenSlidePagerAdapter) pagerAdapter).addFragment(perMonth.toArray(new Parcelable[0]));
                                        prevMonth = date.substring(0, 2);

                                        perMonth = new ArrayList<>();
                                        mCount++;
                                    }
                                    perMonth.add(T);
                                }
                                ((ScreenSlidePagerAdapter) pagerAdapter).addFragment(perMonth.toArray(new Parcelable[0]));

                                Log.d("Transaction.mCount", String.valueOf(mCount));
                                pagerAdapter.saveState();
                                viewPager.setAdapter(pagerAdapter);
                            }
                        });
                    }
                });

        return view;
    }


    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        private ArrayList<Fragment> items = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm, Lifecycle lc) {
            super(fm, lc);
        }

        public void addFragment(Parcelable[] transactions) {
            if(transactions.length == 0) return;

            // Get full name of the month
            int monthIndex = Integer.parseInt(((TransactionParcel) transactions[0]).getDate().substring(0, 2));
            String month = new DateFormatSymbols().getMonths()[monthIndex-1];

            Fragment f = new HistorySlideFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArray("transactions", transactions);
            bundle.putString("month", month);
            f.setArguments(bundle);
            items.add(f);
        }

        @Override
        public Fragment createFragment(int position) {
            return items.get(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}
