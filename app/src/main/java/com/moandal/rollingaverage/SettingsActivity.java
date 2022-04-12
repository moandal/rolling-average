package com.moandal.rollingaverage;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
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

            EditTextPreference rolling_number = getPreferenceManager().findPreference("rolling_number");
            EditTextPreference number_to_display = getPreferenceManager().findPreference("number_to_display");
            EditTextPreference decimal_places = getPreferenceManager().findPreference("decimal_places");

            rolling_number.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int val = Integer.parseInt(newValue.toString());

                    if (val < 2 || val > 100) {
                        Utils.showErrorMessage("Value must be between 2 and 100", getActivity());
                        return false;
                    } else {
                        return true;
                    }
                }
            });

            number_to_display.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int val = Integer.parseInt(newValue.toString());

                    if (val < 1 || val > 100) {
                        Utils.showErrorMessage("Value must be between 1 and 100", getActivity());
                        return false;
                    } else {
                        return true;
                    }
                }
            });

            decimal_places.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int val = Integer.parseInt(newValue.toString());

                    if (val < 1 || val > 5) {
                        Utils.showErrorMessage("Value must be between 1 and 5", getActivity());
                        return false;
                    } else {
                        return true;
                    }
                }
            });
        }

    }

}
