package com.jackpf.apkdownloader.Request;

import java.security.InvalidParameterException;

import com.jackpf.apkdownloader.Downloader;
import com.jackpf.apkdownloader.Entity.App;
import com.jackpf.apkdownloader.Entity.Response;
import com.jackpf.apkdownloader.Model.RequestInterface;
import com.jackpf.apkdownloader.Service.Authenticator;
import com.jackpf.apkdownloader.Service.PlayApi;

public class DownloadRequest extends RequestInterface
{
    @Override
    public Response call(Object ...params)
    {
        try {
            Authenticator authenticator = (Authenticator) getParam(0, Authenticator.class);
            Downloader downloader = (Downloader) getParam(1, Downloader.class);
            String appId = (String) getParam(2, String.class);
            
            download(authenticator, downloader, appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void download(Authenticator authenticator, Downloader downloader, String appId) throws Exception
    {
        PlayApi api = new PlayApi(authenticator);
        
        App app = api.getApp(appId);
        
        downloader.download(app);
    }
}
