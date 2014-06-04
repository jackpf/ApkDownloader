package com.jackpf.apkdownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.jackpf.apkdownloader.Service.Authenticator;

public class PreferencesActivity extends PreferenceActivity implements OnPreferenceClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        findPreference(getString(R.string.pref_device_reset_key)).setOnPreferenceClickListener(this);
    }
    
    /**
     * Set up default preferences
     * 
     * @param context
     */
    public static void setDefaults(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        
        String key = context.getString(R.string.pref_gsfid_key);
        String def = context.getString(R.string.pref_email_default);
        
        String gsfid = prefs.getString(key, def);
        
        if (gsfid.equals("")) {
            editor.putString(key, new Authenticator(context).getGsfId());
            editor.commit();
        }
    }
    
    /**
     * Reset defaults click listener
     * 
     * @param pref
     */
    @Override
    public boolean onPreferenceClick(Preference pref)
    {
        if(pref.getKey().equals(getString(R.string.pref_device_reset_key))){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(getString(R.string.pref_sdk_version_key),              getString(R.string.pref_sdk_version_default));
            editor.putString(getString(R.string.pref_device_and_sdk_version_key),   getString(R.string.pref_device_and_sdk_version_default));
            editor.putString(getString(R.string.pref_operator_key),                 getString(R.string.pref_operator_default));
            editor.putString(getString(R.string.pref_operator_numeric_key),         getString(R.string.pref_operator_numeric_default));
            editor.putString(getString(R.string.pref_locale_key),                   getString(R.string.pref_locale_default));
            editor.putString(getString(R.string.pref_country_key),                  getString(R.string.pref_country_default));

            editor.commit();
            
            Toast.makeText(getApplicationContext(), getString(R.string.device_prefs_reset), Toast.LENGTH_LONG).show();
            
            finish();
            
            return true;
        }
        return false;
    }
}
