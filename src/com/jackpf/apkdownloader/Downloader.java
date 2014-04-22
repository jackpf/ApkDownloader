package com.jackpf.apkdownloader;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.jackpf.apkdownloader.Entity.App;

public class Downloader
{
    private Context context;
    
    public Downloader(Context context)
    {
        this.context = context;
    }
    
    public void download(App app, String authToken)
    {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        
        Request request = new DownloadManager.Request(Uri.parse(app.getDownloadPath()))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(app.getAppId())
            .setDescription("App downloading...")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, app.getAppId() + ".apk")
            .addRequestHeader("Cookie", "MarketDA=" + app.getMarketDA()) // + ";ANDROIDSECURE=" + authToken)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        ;
        
        dm.enqueue(request);
    }
}
