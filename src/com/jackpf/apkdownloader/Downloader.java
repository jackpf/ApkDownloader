package com.jackpf.apkdownloader;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.jackpf.apkdownloader.Entity.App;
import com.jackpf.apkdownloader.Model.UIInterface;

public class Downloader
{
    /**
     * Context
     */
    private Context context;
    
    /**
     * UI
     */
    private UIInterface ui;
    
    /**
     * Download dir
     */
    public static final String DOWNLOAD_DIR = "ApkDownloads";
    
    /**
     * Constructor
     * 
     * @param context
     */
    public Downloader(Context context, UIInterface ui)
    {
        this.context = context;
        this.ui = ui;
    }
    
    /**
     * Download an app
     * 
     * @param app
     */
    @SuppressLint("NewApi")
    public void download(final App app)
    {
        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        
        Request request = new DownloadManager.Request(Uri.parse(app.getDownloadPath()))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(true)
            .setTitle(app.getAppId())
            .setDescription(context.getString(R.string.app_downloading))
            .setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DOWNLOAD_DIR, app.getAppId() + ".apk")
            .addRequestHeader("Cookie", "MarketDA=" + app.getMarketDA()) // + ";ANDROIDSECURE=" + authToken)
        ;
        
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        
        dm.enqueue(request);
        
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    ui.initialise((Object) null);
                    
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    boolean install = prefs.getBoolean(context.getString(R.string.pref_autoinstall_key), Boolean.valueOf(context.getString(R.string.pref_autoinstall_default)));
                    
                    if (install) {
                        Bundle extras = intent.getExtras();
                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                        Cursor c = dm.query(q);

                        if (c.moveToFirst()) {
                            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                String path = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                                Helpers.installFile(context, new File(path));
                            }
                        }
                    }
                }
                
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
