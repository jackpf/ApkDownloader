package com.jackpf.apkdownloader.Service;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings.Secure;

import com.gc.android.market.api.MarketSession;

public class Authenticator
{
    private Context context;
    
    private MarketSession session;
    
    public Authenticator(Context context)
    {
        this.context = context;
    }
    
    public String getToken()
    {
        if (session == null) {
            MarketSession session = new MarketSession(true);
            session.login("jack.philip.farrelly@gmail.com", "hgdu owsf ejhy skjf", getGsfAndroidId());
        }
        
        return session.getAuthSubToken();
    }
    
    public String getAndroidId()
    {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }
    
    public String getGsfAndroidId() 
    {
        Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
        String ID_KEY = "android_id";
        String params[] = {ID_KEY};
        Cursor c = context.getContentResolver().query(URI, null, null, params, null);
        if (!c.moveToFirst() || c.getColumnCount() < 2)
            return null;
        try 
        {
            return Long.toHexString(Long.parseLong(c.getString(1)));
        } 
        catch (NumberFormatException e) 
        {
            return null;
        }
    }
}
