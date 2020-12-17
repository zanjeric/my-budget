package com.example.mybudget;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryPieChart {
    PieChart pieChart;

    public void render(View view, int id,Map<String, Integer> typeAmountMap) {
        pieChart = view.findViewById(id);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();

       for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.animateX(1400);
    }
}
