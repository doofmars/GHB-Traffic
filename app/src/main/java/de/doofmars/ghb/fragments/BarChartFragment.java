package de.doofmars.ghb.fragments;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.doofmars.ghb.MyValueFormatter;
import de.doofmars.ghb.R;
import de.doofmars.ghb.model.TrafficReport;

/**
 * Created by Jan on 16.03.2015.
 */
public class BarChartFragment extends Fragment  {

    protected BarChart mChart;
    private TrafficReport report;
    private ValueFormatter mValueFormatter = new MyValueFormatter();
//    private Typeface mTf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_barchart, container, false);

        mChart = (BarChart) rootView.findViewById(R.id.barChart);

        mChart.setDrawBarShadow(true);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setTouchEnabled(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

//        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8);
        leftAxis.setValueFormatter(mValueFormatter);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


        Intent intent = getActivity().getIntent();
        report = (TrafficReport) intent.getSerializableExtra("trafficReport");

        mChart.getAxisRight().setEnabled(false);

        this.update();

        mChart.getLegend().setEnabled(false);

        return rootView;
    }

    private void update() {

        ArrayList<String> xVals = (ArrayList) report.getDaysText();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<Float> vals = report.getDaysTotal();
        int counter = 0;
        for (Float val : vals) {
            yVals1.add(new BarEntry(val, counter));
            counter++;
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
        set1.setBarSpacePercent(10f);
        set1.setValueFormatter(mValueFormatter);
        set1.setColors(report.getDaysColors(), getActivity().getApplicationContext());

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
//        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(10f);
//        data.setValueTypeface(mTf);

        mChart.setData(data);
    }
}
