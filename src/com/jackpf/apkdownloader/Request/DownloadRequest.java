package com.jackpf.apkdownloader.Request;

import com.jackpf.apkdownloader.Downloader;
import com.jackpf.apkdownloader.Entity.Response;
import com.jackpf.apkdownloader.Exception.AuthenticationException;
import com.jackpf.apkdownloader.Exception.PlayApiException;
import com.jackpf.apkdownloader.Model.RequestInterface;
import com.jackpf.apkdownloader.Service.Authenticator;
import com.jackpf.apkdownloader.Service.PlayApi;

public class DownloadRequest extends RequestInterface
{
    @Override
    public Response call(Object ...params) throws Exception
    {
        Authenticator authenticator = (Authenticator) getParam(params, 0, Authenticator.class);
        Downloader downloader       = (Downloader) getParam(params, 1, Downloader.class);
        String appId                = (String) getParam(params, 2, String.class);
        
        download(authenticator, downloader, appId);
        
        return null;
    }
    
    private void download(Authenticator authenticator, Downloader downloader, String appId)
        throws AuthenticationException, PlayApiException
    {
        PlayApi api = new PlayApi(authenticator);
        downloader.download(api.getApp(appId));
    }
}
