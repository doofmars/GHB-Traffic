package de.doofmars.ghb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
public class GeneralFragment extends Fragment {

    private TrafficReport report;
    private String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_general, container, false);

        Intent intent = getActivity().getIntent();
        report = (TrafficReport) intent.getSerializableExtra(getResources().getString(R.string.traffic_report_key));
        message = (String) intent.getSerializableExtra(getResources().getString(R.string.message_key));

        TextView tv = (TextView) rootView.findViewById(R.id.tv_dynamic);
        ImageView iv = (ImageView) rootView.findViewById(R.id.iv_pull_arrow);

        if (message != null){
            tv.setText(message);
            iv.setVisibility(View.INVISIBLE);
        }
        if (report != null){
            tv.setText(new CustomValueFormatter().getFormattedValue(report.getTotal())  + " / 25GB used");
            iv.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }

}
