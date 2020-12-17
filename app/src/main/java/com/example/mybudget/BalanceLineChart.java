package com.example.mybudget;

import android.graphics.Color;
import android.view.View;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class ClaimsXAxisValueFormatter extends ValueFormatter {
    List<String> datesList;

    public ClaimsXAxisValueFormatter(List<String> arrayOfDates) {
        this.datesList = arrayOfDates;
    }

    public static long getDateInMilliSeconds(String givenDateString, String format) {
        String DATE_TIME_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.GERMANY);
        long timeInMilliseconds = 1;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        Integer position = Math.round(value);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");

        if (value >= 0 && value < 1) {
            position = 0;
        } else if (value >= 1 && value < 2) {
            position = 1;
        } else if (value >= 2 && value < 3) {
            position = 2;
        } else if (value >= 3 && value < 4) {
            position = 3;
        }
        if (position <= datesList.size())
            return sdf.format(new Date((getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd"))));
        return "";
    }
}

class ClaimsYAxisValueFormatter extends ValueFormatter {

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return value + "€";
    }
}

public class BalanceLineChart{
    private LineChart lineChart;

    private void setChartProps() {
        lineChart.setBackgroundColor(Color.parseColor("#3AB44B"));
        lineChart.setTouchEnabled(true);
        lineChart.setClickable(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);

        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getAxisLeft().setDrawAxisLine(false);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextSize(12f);

        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.setExtraOffsets(40,20,40,20);
    }

    public void render(View view, int id, List<String> dates, List<Double> amounts) {
        // LineChart
        lineChart = view.findViewById(id);

        // Set chart properties
       setChartProps();

        XAxis xAxis = lineChart.getXAxis();
        xAxis.enableGridDashedLine(5f, 15f, 0f);
        xAxis.setAxisMaximum(4f);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(5, true);
        xAxis.setValueFormatter(new ClaimsXAxisValueFormatter(dates));

        LimitLine ll2 = new LimitLine(35f, "");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        //ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setLineColor(Color.parseColor("#FFFFFF"));


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setValueFormatter(new ClaimsYAxisValueFormatter());
        lineChart.animateY(1000);

        // Amounts
        ArrayList<Entry> values = new ArrayList<>();
        for(int i=0; i<amounts.size();i++) {
            values.add(new Entry(i, amounts.get(i).floatValue()));
        }

        LineDataSet set1;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Total balance");
            set1.setDrawCircles(true);
            set1.enableDashedLine(10f, 0f, 0f);
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(3f);//line size
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(10f);
            set1.setDrawFilled(true);
            set1.setValueTextColor(Color.WHITE);
            set1.setFormLineWidth(5f);
            //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(5.f);

            set1.setDrawValues(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);

            lineChart.setData(data);
        }
    }

}
