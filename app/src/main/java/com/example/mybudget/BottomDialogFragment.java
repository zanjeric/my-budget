package com.example.mybudget;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;



public class BottomDialogFragment extends BottomSheetDialogFragment {
    private FirebaseFirestore db;
    private RadioButton rbLeft, rbRight;
    private TextView operatorText;
    private EditText amountText;
    private Spinner categorySpinner;
    private int amountNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_page_up, container, false);

        /* Firestore Database */
        db = FirebaseFirestore.getInstance();

        /* Radio buttons */
        rbLeft= view.findViewById(R.id.rbLeft);
        rbRight = view.findViewById(R.id.rbRight);
        /* Radio button event listeners */
        rbLeft.setOnClickListener(onRadioBtnClicked);
        rbRight.setOnClickListener(onRadioBtnClicked);

        operatorText = view.findViewById(R.id.sign);

        /* Amount input */
        amountText = view.findViewById(R.id.amountValue);
        amountNumber = Integer.parseInt(amountText.getText().toString());

        /* Choose category */
        categorySpinner = view.findViewById(R.id.categorySpinner);
        String[] categoryArray = {"Food","Health","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,categoryArray);
        categorySpinner.setAdapter(adapter);

        /* Save transaction to DB */
        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
                    }
                    break;
                case R.id.rbRight:
                    /* Income */
                    if (isSelected) {
                        operatorText.setText("+");
                        rbRight.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                        rbLeft.setTextColor(ContextCompat.getColor(getContext(),R.color.primaryColor));
                    }
                    break;
            }
        }
    };
}
