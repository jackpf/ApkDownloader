package com.jackpf.apkdownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.jackpf.apkdownloader.Service.Authenticator;

public class PreferencesActivity extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
    }
    
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
}
