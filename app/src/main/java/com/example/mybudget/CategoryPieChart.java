package com.example.mybudget;

import android.graphics.Color;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryPieChart {
    PieChart pieChart;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void render(View view, int id, Map<String, Double> typeAmountMap, Map<String, String> colorMap) {
        pieChart = view.findViewById(id);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();

//        for (int c : ColorTemplate.MATERIAL_COLORS)
//            colors.add(c);

        typeAmountMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(stringDoubleEntry -> {
                    pieEntries.add(new PieEntry(typeAmountMap.get(stringDoubleEntry.getKey()).floatValue(), stringDoubleEntry.getKey()));
                    colors.add(ColorTemplate.rgb(colorMap.getOrDefault(stringDoubleEntry.getKey(), "#3498db")));
                });

//        for(String type: typeAmountMap.keySet()){
//            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
//            colors.add(ColorTemplate.rgb(colorMap.getOrDefault(type, "#3498db")));
//        }

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
