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

import de.doofmars.ghb.api.DataLoader;
import de.doofmars.ghb.fragments.BarChartFragment;
import de.doofmars.ghb.fragments.GeneralFragment;
import de.doofmars.ghb.fragments.PieChartFragment;
import de.doofmars.ghb.model.TrafficReport;


public class Statistics extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TRAFFIC_CALL_GHB = "https://ghb.hs-furtwangen.de/api/call?method=t&key=";
    private final static String TRAFFIC_CALL_ASK = "https://ask.hs-furtwangen.de/api/call?method=t&key=";
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
        if (preferences.contains("api_key")) {
            if (preferences.getString("dorm_name", getString(R.string.default_dorm_name)).equals(getString(R.string.default_dorm_name))) {
                new DataLoader(this).execute(TRAFFIC_CALL_GHB + preferences.getString("api_key", ""));
            } else {
                new DataLoader(this).execute(TRAFFIC_CALL_ASK + preferences.getString("api_key", ""));
            }
        } else {
            //api-key is not yet set
            swipeLayout.setRefreshing(false);
            getIntent().putExtra(getString(R.string.traffic_report_key), new TrafficReport(getString(R.string.message_api_key)));

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_general, new GeneralFragment()).commit();
        }
    }

    /**
     * Function is called if async task in DataLoader has processed the data
     * @param report the generated report
     */
    public void onBackgroundTaskCompleted(TrafficReport report) {
        swipeLayout.setRefreshing(false);

        getIntent().putExtra(getString(R.string.traffic_report_key), report);
        GeneralFragment generalFragment = new GeneralFragment();
        FragmentManager manager = getSupportFragmentManager();

        if (!report.hasMessage()) {
            PieChartFragment pieChartFragment = new PieChartFragment();
            BarChartFragment barChartFragment = new BarChartFragment();

            manager.beginTransaction()
                    .replace(R.id.container_general, generalFragment)
                    .replace(R.id.container_bar_chart, barChartFragment)
                    .replace(R.id.container_pie_chart, pieChartFragment).commit();
        } else {
            manager.beginTransaction()
                    .replace(R.id.container_general, generalFragment).commit();
        }

    }
}
