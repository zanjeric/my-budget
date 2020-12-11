package com.example.mybudget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Line chart for balance
        BalanceLineChart balanceLineChart = new BalanceLineChart();
        List<String> dates = new ArrayList<>();
        dates.add("2020-02-20");
        dates.add("2020-05-11");
        dates.add("2020-07-05");
        dates.add("2020-08-15");
        dates.add("2020-12-02");

        List<Double> amounts = new ArrayList<>();
        amounts.add(200.2);
        amounts.add(500.2);
        amounts.add(800.2);
        amounts.add(100.2);
        amounts.add(330.2);

        balanceLineChart.render(view,R.id.balanceChartStatistics,dates,amounts);

        return view;
    }
}
