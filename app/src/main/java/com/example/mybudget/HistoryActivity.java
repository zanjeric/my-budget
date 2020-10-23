package com.example.mybudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        /* Bottom menu */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setSelectedItemId(R.id.menuHistory);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuHome:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuHistory:
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuStatistics:
                        startActivity(new Intent(getApplicationContext(), StatisticsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuSettings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        /* ADD BUTTON */
        FloatingActionButton btnOpenPage = findViewById(R.id.btnOpenPage);
        btnOpenPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HistoryActivity.this, R.style.Theme_Design_BottomSheetDialog);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_page_up,(LinearLayout)findViewById(R.id.pageUpContainer));
                bottomSheetView.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }
}