package com.jackpf.apkdownloader;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

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
    public void download(App app)
    {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        
        Request request = new DownloadManager.Request(Uri.parse(app.getDownloadPath()))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(true)
            .setTitle(app.getAppId())
            .setDescription("App downloading...")
            .setDestinationInExternalPublicDir(DOWNLOAD_DIR, app.getAppId() + ".apk")
            .addRequestHeader("Cookie", "MarketDA=" + app.getMarketDA()) // + ";ANDROIDSECURE=" + authToken)
        ;
        
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ui.initialise((Object) null);
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        
        dm.enqueue(request);
    }
}
