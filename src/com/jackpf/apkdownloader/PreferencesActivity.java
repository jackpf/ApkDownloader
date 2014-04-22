package com.jackpf.apkdownloader;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity
{
    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
    }
}
