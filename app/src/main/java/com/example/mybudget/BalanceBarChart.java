package com.example.mybudget;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Map;

public class BalanceBarChart {
    BarChart barChart;

    private void initializeBarChart() {
        barChart.getDescription().setEnabled(false);

        barChart.setMaxVisibleValueCount(4);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setPinchZoom(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.animateY(1500);
        barChart.getLegend().setEnabled(false);

        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisLeft().setDrawLabels(true);
        barChart.setTouchEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.invalidate();
        barChart.animateY(1500);

    }

    public void render(View view, int id) {
        barChart = view.findViewById(id);
        initializeBarChart();

        ArrayList<BarEntry> transactions = new ArrayList<>();
        transactions.add(new BarEntry(2014,420));
        transactions.add(new BarEntry(2015,220));
        transactions.add(new BarEntry(2016,120));
        transactions.add(new BarEntry(2017,620));
        transactions.add(new BarEntry(2018,820));

        BarDataSet barDataSet = new BarDataSet(transactions,"transactions");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
    }
}
