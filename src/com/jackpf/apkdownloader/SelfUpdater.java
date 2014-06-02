package com.jackpf.apkdownloader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Xml;
import android.widget.Toast;

public class SelfUpdater
{
    protected Context context;
    
    protected final static String   PREF_UPDATE_KEY = "last_update_time";
    protected final static int      UPDATE_INTERVAL = 60 * 60 * 24;
    
    public SelfUpdater(Context context)
    {
        this.context = context;
    }
    
    public void checkForUpdate()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        long lastUpdateTime = prefs.getLong(PREF_UPDATE_KEY, 0), currentTime = System.currentTimeMillis() / 1000;
        
        if (currentTime - lastUpdateTime <= UPDATE_INTERVAL) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREF_UPDATE_KEY, currentTime);
        editor.commit();
        
        new Thread() {
            @Override
            public void run() {
                try {
                    check();
                } catch (Exception e) {
                    Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }.start();
    }
    
    protected void check()
        throws IOException, MalformedURLException, XmlPullParserException, PackageManager.NameNotFoundException
    {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        int currentVersionCode = packageInfo.versionCode;
        String currentVersionName = packageInfo.versionName;
        
        XmlPullParser parser = Xml.newPullParser();
        
        parser.setInput(new URL(context.getString(R.string.update_manifest)).openStream(), null);
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("manifest")) {
                int versionCode = Integer.parseInt(parser.getAttributeValue(null, "versionCode"));
                String versionName = parser.getAttributeValue(null, "versionName");
                
                if (versionCode > currentVersionCode) {
                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.update_apk)));
                    
                    Notification notification = new NotificationCompat.Builder(context)
                        .setContentTitle(context.getString(R.string.update_title))
                        .setContentText(context.getString(R.string.update_text, currentVersionName, versionName))
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
                        .build()
                    ;
                    
                    nm.notify(0, notification);
                }
            }
        }
    }
}
