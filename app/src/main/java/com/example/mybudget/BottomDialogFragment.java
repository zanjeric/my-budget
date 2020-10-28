package com.example.mybudget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class Category {
    public String id;
    public String name;
    public String color;

    public Category(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}

public class BottomDialogFragment extends BottomSheetDialogFragment {
    private FirebaseFirestore db;
    private RadioButton rbLeft, rbRight;
    private TextView operatorText;
    private EditText amountText;
    private Spinner categorySpinner;
    private int amountNumber;
    private boolean isIncome = false;
    private static final String TAG = "Fetch categories";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_page_up, container, false);

        /* Firestore Database */
        db = FirebaseFirestore.getInstance();

        /* Get current user */
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        /* Radio buttons */
        rbLeft= view.findViewById(R.id.rbLeft);
        rbRight = view.findViewById(R.id.rbRight);
        /* Radio button event listeners */
        rbLeft.setOnClickListener(onRadioBtnClicked);
        rbRight.setOnClickListener(onRadioBtnClicked);

        operatorText = view.findViewById(R.id.sign);

        /* Amount input */
        amountText = view.findViewById(R.id.amountValue);


        /* Choose category */
        categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayList<String> categoryNameArray = new ArrayList<>();
        ArrayList<String> categoryIdArray = new ArrayList<>();
        categoryNameArray.add("Select Category...");
        categoryIdArray.add("0");
        /* Get categories from DB */
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.get("name").toString();
                                //String color = document.get("color").toString();
                                //Category category = new Category(id,name,color);
                                categoryNameArray.add(name);
                                categoryIdArray.add(id);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,categoryNameArray);
        categorySpinner.setAdapter(adapter);

        /* Save transaction to DB */
        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountNumber = Integer.parseInt(amountText.getText().toString());
                if(!isIncome) {
                    amountNumber *= -1;
                }

                if(categorySpinner.getSelectedItemPosition() != 0 && amountNumber != 0) {

                    Map<String, Object> docData = new HashMap<>();
                    docData.put("UID", db.document("users/" + uid));
                    docData.put("amount", amountNumber);
                    docData.put("category", db.document("categories/" + categoryIdArray.get(categorySpinner.getSelectedItemPosition())));
                    docData.put("date", new Timestamp(new Date()));
                    docData.put("title", "");

                    db.collection("transactions").document()
                            .set(docData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                    dismiss();
                }
                else if(amountNumber == 0) {
                    Toast.makeText(getContext(), "Please enter a valid number!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Please select a category!", Toast.LENGTH_LONG).show();
                }
            }
        });
        //setStyle(STYLE_NORMAL,R.style.Theme_Design_BottomSheetDialog);

        return view;
    }



    private View.OnClickListener onRadioBtnClicked = new View.OnClickListener()  {
        public void onClick(View view) {
            boolean isSelected = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.rbLeft:
                    /* Expense */
                    if (isSelected) {
                        operatorText.setText("-");
                        rbLeft.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                        rbRight.setTextColor(ContextCompat.getColor(getContext(),R.color.primaryColor));
                        isIncome = false;
                    }
                    break;
                case R.id.rbRight:
                    /* Income */
                    if (isSelected) {
                        operatorText.setText("+");
                        rbRight.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                        rbLeft.setTextColor(ContextCompat.getColor(getContext(),R.color.primaryColor));
                        isIncome = true;
                    }
                    break;
            }
        }
    };

}
