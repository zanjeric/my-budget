package com.example.mybudget;

import android.content.Context;
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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class BottomDialogFragment extends BottomSheetDialogFragment {
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_page_up, container, false);

        /* Firestore Database */
        db = FirebaseFirestore.getInstance();

        /* Radio buttons */
        view.findViewById(R.id.rbLeft).setOnClickListener(onRadioBtnClicked);
        view.findViewById(R.id.rbRight).setOnClickListener(onRadioBtnClicked);

        /* Amount input */
        EditText amountText = view.findViewById(R.id.amountValue);
        String amount = amountText.getText().toString();

        /* Choose category */
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    private View.OnClickListener onRadioBtnClicked = new View.OnClickListener()  {
        public void onClick(View view) {
            RadioButton rbLeft = view.findViewById(R.id.rbLeft);
            RadioButton rbRight = view.findViewById(R.id.rbRight);
            boolean isSelected = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.rbLeft:
                    if (isSelected) {
                        /*TextView signText = view.findViewById(R.id.sign);
                        signText.setText("-");*/
                        /*rbLeft.setTextColor(getResources().getColor(R.color.white));
                        rbRight.setTextColor(getResources().getColor(R.color.primaryColor));*/

                        Toast.makeText(getContext(), "left", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.rbRight:
                    if (isSelected) {
                        /*TextView signText = view.findViewById(R.id.sign);
                        signText.setText("+");*/
                        /*rbRight.setTextColor(getResources().getColor(R.color.primaryColor));
                        rbLeft.setTextColor(getResources().getColor(R.color.white));*/
                        Toast.makeText(getContext(), "right", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
}
