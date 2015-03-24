package de.doofmars.ghb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import de.doofmars.ghb.util.CustomValueFormatter;
import de.doofmars.ghb.R;
import de.doofmars.ghb.model.TrafficReport;

/**
 * Fragment to display the data in an bar chart
 */
public class BarChartFragment extends Fragment  {

    protected BarChart mChart;
    private TrafficReport report;
    private ValueFormatter mValueFormatter = new CustomValueFormatter();

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
        mChart.setTouchEnabled(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8);
        leftAxis.setValueFormatter(mValueFormatter);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        //Load the data
        Intent intent = getActivity().getIntent();
        report = (TrafficReport) intent.getSerializableExtra(getResources().getString(R.string.traffic_report_key));
        this.update();

        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        return rootView;
    }

    /**
     * Update function to fill the chart
     */
    private void update() {

        ArrayList xVals = (ArrayList) report.getDaysText();
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
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

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        mChart.setData(data);
    }
}
