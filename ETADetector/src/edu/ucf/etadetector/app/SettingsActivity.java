package edu.ucf.etadetector.app;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.widget.EditText;

public class SettingsActivity extends PreferenceActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_settings);
        initialize();
    }
    
    private void initialize() {
    	EditTextPreference ap1MacAddrEditTextPref = (EditTextPreference) findPreference("pref_ap1_mac_addr");
    	EditTextPreference ap2MacAddrEditTextPref = (EditTextPreference) findPreference("pref_ap2_mac_addr");
    	EditText ap1MacAddrEditText = ap1MacAddrEditTextPref.getEditText();
    	EditText ap2MacAddrEditText = ap2MacAddrEditTextPref.getEditText();
    	ap1MacAddrEditText.addTextChangedListener(new MACAddrTextWatcher(ap1MacAddrEditText));
    	ap2MacAddrEditText.addTextChangedListener(new MACAddrTextWatcher(ap2MacAddrEditText));
    }
    
}
