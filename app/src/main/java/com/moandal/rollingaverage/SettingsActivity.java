package com.moandal.rollingaverage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import androidx.appcompat.app.ActionBar;

import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_frag, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    // The preference summary shows the current value of the preference under the preference title on the setting screen
    // This method sets the summary value so that it shows the correct value
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            int val = Integer.parseInt(value.toString());

            if (preference.getKey().equals("rolling_number")) {
                if (val < 2 || val > 100) {
                    preference.setSummary(preferences.getString("rolling_number", "7"));
                    return false;
                } else {
                    preference.setSummary(value.toString());
                    return true;
                }
            }
            else if (preference.getKey().equals("number_to_display")) {
                if (val < 1 || val > 100) {
                    preference.setSummary(preferences.getString("number_to_display", "7"));
                    return false;
                } else {
                    preference.setSummary(value.toString());
                    return true;
                }
            }
            else {
                preference.setSummary(value.toString());
                return true;
            }
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    /*

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    private void setupSimplePreferencesScreen() {

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.root_preferences);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("rolling_number"));
        bindPreferenceSummaryToValue(findPreference("decimal_places"));
        bindPreferenceSummaryToValue(findPreference("number_to_display"));
    }
*/
}
