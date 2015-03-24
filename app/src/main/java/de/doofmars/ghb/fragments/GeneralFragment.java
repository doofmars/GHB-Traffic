package de.doofmars.ghb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.doofmars.ghb.R;
import de.doofmars.ghb.model.TrafficReport;
import de.doofmars.ghb.util.CustomValueFormatter;

public class GeneralFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_general, container, false);

        Intent intent = getActivity().getIntent();
        TrafficReport report = (TrafficReport) intent.getSerializableExtra(getResources().getString(R.string.traffic_report_key));

        TextView tv = (TextView) rootView.findViewById(R.id.tv_dynamic);
        ImageView iv = (ImageView) rootView.findViewById(R.id.iv_pull_arrow);

        if (report != null) {
            if (report.hasMessage()) {
                tv.setText(report.getMessage());
                iv.setVisibility(View.INVISIBLE);
            } else {
                tv.setText(new CustomValueFormatter().getFormattedValue(report.getTotal()) + " / 25GB used");
                iv.setVisibility(View.INVISIBLE);
            }
        }
        return rootView;
    }

}
