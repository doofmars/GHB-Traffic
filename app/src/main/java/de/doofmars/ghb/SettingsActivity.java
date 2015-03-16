package de.doofmars.ghb;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;


import java.util.List;

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
