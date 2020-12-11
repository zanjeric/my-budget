package com.example.mybudget;

import android.graphics.Color;
import android.graphics.DashPathEffect;
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

        if (value > 1 && value < 2) {
            position = 0;
        } else if (value > 2 && value < 3) {
            position = 1;
        } else if (value > 3 && value < 4) {
            position = 2;
        } else if (value > 4 && value <= 5) {
            position = 3;
        }
        if (position < datesList.size())
            return sdf.format(new Date((getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd"))));
        return "";
    }
}

class ClaimsYAxisValueFormatter extends ValueFormatter {

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return value + "k";
    }
}

public class BalanceLineChart{
    private LineChart lineChart;

    private void setChartProps() {
        lineChart.setBackgroundColor(Color.parseColor("#666666"));
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
        //lineChart.getXAxis().setDrawGridLines(false);
        //lineChart.getXAxis().setLabelCount(2);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextSize(12f);
        //lineChart.getXAxis().setDrawLabels(false);
        //lineChart.getXAxis().setDrawAxisLine(false);
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

       /////////////////////////////////////////
        /*List<String> dates = new ArrayList<>();
        dates.add("2020-05-25");
        dates.add("2020-03-25");
        dates.add("2020-04-25");
        dates.add("2020-01-25");

        List<Double> amounts = new ArrayList<>();
        amounts.add(1.1);
        amounts.add(10.5);
        amounts.add(23.2);
        amounts.add(23.3);*/

       renderDates(dates,amounts);


        ////////////////////////////////////////////////////////////////////
        /*List<String> dates = new ArrayList<>();
        dates.add("1");
        dates.add("4");
        dates.add("14");
        dates.add("21");
        lineChart.getXAxis().setValueFormatter(new ClaimsXAxisValueFormatter(dates));
        lineChart.getAxisLeft().setValueFormatter(new ClaimsYAxisValueFormatter());

        // Add data
        ArrayList<Entry> balanceValues = new ArrayList<>();
        balanceValues.add(new Entry(1,160));
        balanceValues.add(new Entry(2,100));
        balanceValues.add(new Entry(3,40));
        balanceValues.add(new Entry(4,60));
        balanceValues.add(new Entry(5,70));

        LineDataSet set1 = new LineDataSet(balanceValues, "Balance");
        set1.setFillAlpha(110);
        set1.setColor(Color.WHITE);
        set1.setLineWidth(3f);
        set1.setValueTextSize(12f);
        set1.setValueTextColor(Color.WHITE);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.animateX(150);*/
    }

    private void renderDates(List<String> dates, List<Double> allAmounts) {
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("1");
        xAxisLabel.add("7");
        xAxisLabel.add("14");
        xAxisLabel.add("21");
        xAxisLabel.add("28");
        xAxisLabel.add("30");

        XAxis xAxis = lineChart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        xAxis.enableGridDashedLine(2f, 7f, 0f);
        xAxis.setAxisMaximum(5f);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(6, true);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(7f);
        xAxis.setLabelRotationAngle(315f);

        xAxis.setValueFormatter(new ClaimsXAxisValueFormatter(dates));

        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawLimitLinesBehindData(true);

        //LimitLine ll1 = new LimitLine(Float.parseFloat(UISetters.getDateInNumber()), UISetters.getDateInNumber());
        //ll1.setLineColor(getResources().getColor(R.color.greyish_brown));
        //ll1.setLineWidth(4f);
        //ll1.enableDashedLine(10f, 10f, 0f);
        //ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        //ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(35f, "");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setLineColor(Color.parseColor("#FFFFFF"));

        xAxis.removeAllLimitLines();
        //xAxis.addLimitLine(ll1);
        xAxis.addLimitLine(ll2);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        //leftAxis.addLimitLine(ll1);
        //leftAxis.addLimitLine(ll2);

        //leftAxis.setAxisMaximum(findMaximumValueInList(allAmounts).floatValue() + 100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        //XAxis xAxis = mBarChart.getXAxis();
        leftAxis.setValueFormatter(new ClaimsYAxisValueFormatter());

        lineChart.getAxisRight().setEnabled(false);

        //setData()-- allAmounts is data to display;
        setDataForWeeksWise(allAmounts);

    }

    private void setDataForWeeksWise(List<Double> amounts) {

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(1, amounts.get(0).floatValue()));
        values.add(new Entry(2, amounts.get(1).floatValue()));
        values.add(new Entry(3, amounts.get(2).floatValue()));
        values.add(new Entry(4, amounts.get(3).floatValue()));

        LineDataSet set1;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Total volume");
            set1.setDrawCircles(true);
            set1.enableDashedLine(10f, 0f, 0f);
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            //set1.setColor(getResources().getColor(R.color.toolBarColor));
            //set1.setCircleColor(getResources().getColor(R.color.toolBarColor));
            set1.setLineWidth(2f);//line size
            set1.setCircleRadius(5f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(10f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(5f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(5.f);

            set1.setDrawValues(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);

            lineChart.setData(data);
        }
    }
}
