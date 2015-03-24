package de.doofmars.ghb;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import de.doofmars.ghb.fragments.SettingsFragment;

/**
 * Empty Activity to hold the SettingsFragment
 * @author preussjan
 * @since 1.0
 * @version 1.0
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
        PreferenceManager.setDefaultValues(SettingsActivity.this, R.xml.preferences, false);
    }

}
