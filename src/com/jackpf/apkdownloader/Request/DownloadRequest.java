package com.jackpf.apkdownloader.Request;

import com.jackpf.apkdownloader.Downloader;
import com.jackpf.apkdownloader.Entity.Response;
import com.jackpf.apkdownloader.Exception.AuthenticationException;
import com.jackpf.apkdownloader.Exception.PlayApiException;
import com.jackpf.apkdownloader.Model.RequestInterface;
import com.jackpf.apkdownloader.Service.PlayApi;

public class DownloadRequest extends RequestInterface
{
    @Override
    public Response call(Object ...params) throws AuthenticationException, PlayApiException
    {
        PlayApi api                 = (PlayApi) getParam(params, 0, PlayApi.class);
        Downloader downloader       = (Downloader) getParam(params, 1, Downloader.class);
        String appId                = (String) getParam(params, 2, String.class);
        
        downloader.download(api.getApp(appId));
        
        return null;
    }
}
