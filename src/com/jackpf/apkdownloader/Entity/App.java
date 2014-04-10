package com.jackpf.apkdownloader.Entity;

public class App
{
    private String appId;
    
    private String downloadPath;
    
    private String marketDA;
    
    public App(String appId, String downloadPath, String marketDA)
    {
        this.appId = appId;
        this.downloadPath = downloadPath;
        this.marketDA = marketDA;
    }
    
    public String getAppId()
    {
        return appId;
    }
    
    public String getDownloadPath()
    {
        return downloadPath;
    }
    
    public String getMarketDA()
    {
        return marketDA;
    }
}
