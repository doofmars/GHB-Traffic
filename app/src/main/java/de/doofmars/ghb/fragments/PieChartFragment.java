package de.doofmars.ghb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;

import de.doofmars.ghb.R;
import de.doofmars.ghb.model.TrafficReport;
import de.doofmars.ghb.util.CustomValueFormatter;

/**
 * Created by Jan on 20.03.2015.
 */
public class PieChartFragment extends Fragment implements OnChartValueSelectedListener {

    private PieChart mChart;
    private TrafficReport report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_piechart, container, false);

        mChart = (PieChart) rootView.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(50f);
        mChart.setDescription("");
        mChart.setDrawCenterText(true);
        mChart.setDrawHoleEnabled(true);
        mChart.setRotationAngle(270);
        mChart.setRotationEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setCenterText(getResources().getString(R.string.pie_chart_center_label));

        //Load the data
        Intent intent = getActivity().getIntent();
        report = (TrafficReport) intent.getSerializableExtra(getResources().getString(R.string.traffic_report_key));
        setData();

        mChart.animateXY(1500, 1500);
// mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

        return rootView;
    }

   private void setData() {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<String> xVals = report.getHosts();

        for (int i = 0; i < xVals.size(); i++) {
            yVals1.add(new Entry(report.getTrafficByHost(xVals.get(i)), i));
        }

        PieDataSet dataSet = new PieDataSet(yVals1, getResources().getString(R.string.pie_chart_legend_label));
        dataSet.setSliceSpace(3f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        mChart.setData(data);
// undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        mChart.setCenterText(report.getHost(e.getXIndex()) + "\n" +new CustomValueFormatter().getFormattedValue(e.getVal()));
    }

    @Override
    public void onNothingSelected() {
        mChart.setCenterText(getResources().getString(R.string.pie_chart_center_label));
    }

}
