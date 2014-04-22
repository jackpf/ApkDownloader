package com.jackpf.apkdownloader.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

import com.gc.android.market.api.LoginException;
import com.gc.android.market.api.MarketSession;
import com.jackpf.apkdownloader.R;
import com.jackpf.apkdownloader.Exception.AuthenticationException;

public class Authenticator
{
    /**
     * Context
     */
    private Context context;
    
    /**
     * Market session
     */
    private MarketSession session;
    
    /**
     * Google login credentials
     */
    private String email, password;
    
    /**
     * Constructor
     * 
     * @param context
     */
    public Authenticator(Context context)
    {
        this.context = context;
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        email = prefs.getString(context.getString(R.string.pref_email_key), context.getString(R.string.pref_email_default));
        password = prefs.getString(context.getString(R.string.pref_password_key), context.getString(R.string.pref_password_default));
    }
    
    /**
     * Get authentication token
     * 
     * @throws AuthenticationException
     * @return
     */
    public String getToken() throws AuthenticationException
    {
        if (session == null) {
            session = new MarketSession(true);
            
            try {
                session.login(email, password, getGsfId());
            } catch (LoginException e) {
                throw new AuthenticationException(e.getMessage());
            }
        }
        
        return session.getAuthSubToken();
    }
    
    /**
     * Get android id
     * 
     * @return
     */
    public String getAndroidId()
    {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }
    
    /**
     * Get google services framework id
     * 
     * @return
     */
    public String getGsfId() 
    {
        Cursor c = context.getContentResolver().query(
            Uri.parse("content://com.google.android.gsf.gservices"),
            null,
            null,
            new String[]{"android_id"},
            null
        );
        
        if (!c.moveToFirst() || c.getColumnCount() < 2) {
            return null;
        }
        
        try {
            return Long.toHexString(Long.parseLong(c.getString(1)));
        } catch (NumberFormatException e) {
            return null;
        } finally {
            c.close();
        }
    }
}
