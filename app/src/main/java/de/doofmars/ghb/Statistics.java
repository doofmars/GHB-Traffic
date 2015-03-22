package de.doofmars.ghb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.doofmars.ghb.api.DataLoader;
import de.doofmars.ghb.fragments.BarChartFragment;
import de.doofmars.ghb.fragments.GeneralFragment;
import de.doofmars.ghb.fragments.PieChartFragment;
import de.doofmars.ghb.model.TrafficReport;


public class Statistics extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TRAFFIC_CALL = "https://ghb.hs-furtwangen.de/api/call?method=t&key=";
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_general, new GeneralFragment())
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(settingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (preferences.contains("api_key")){
            new DataLoader(this).execute(TRAFFIC_CALL + preferences.getString("api_key", ""));
        } else {
            swipeLayout.setRefreshing(false);
            getIntent().putExtra(getString(R.string.message_key), getString(R.string.message_api_key));

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_general, new GeneralFragment()).commit();
        }
    }

    public void onBackgroundTaskCompleted(TrafficReport report) {
        swipeLayout.setRefreshing(false);

        getIntent().putExtra(getResources().getString(R.string.traffic_report_key), report);

        PieChartFragment pieChartFragment = new PieChartFragment();
        BarChartFragment barChartFragment = new BarChartFragment();
        GeneralFragment generalFragment = new GeneralFragment();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.container_general, generalFragment)
                .replace(R.id.container_bar_chart, barChartFragment)
                .replace(R.id.container_pie_chart, pieChartFragment).commit();
    }
}
