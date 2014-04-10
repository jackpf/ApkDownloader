package com.jackpf.apkdownloader.Request;

import java.security.InvalidParameterException;

import com.gc.android.market.api.MarketSession;
import com.jackpf.apkdownloader.Downloader;
import com.jackpf.apkdownloader.Entity.App;
import com.jackpf.apkdownloader.Entity.Response;
import com.jackpf.apkdownloader.Model.RequestInterface;
import com.jackpf.apkdownloader.Service.PlayApi;

public class TestRequest extends RequestInterface
{
    @Override
    public Response call(Object ...params)
    {
        try {
            if (params.length < 1 || !(params[0] instanceof Downloader)) {
                throw new InvalidParameterException("No download manager");
            }
            
            Downloader downloader = (Downloader) params[0];

            if (params.length < 2 || !(params[1] instanceof String)) {
                throw new InvalidParameterException("No app id");
            }
            
            String appId = (String) params[1];
            
            search(downloader, appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void search(Downloader downloader, String appId) throws Exception
    {
        MarketSession session = new MarketSession(true);
        session.login("jack.philip.farrelly@gmail.com", "bjfg yjdr giqc pvkn", "129596a2a13cb718");
        
        PlayApi api = new PlayApi(session.getAuthSubToken(), "3b718fcb3fa05cba");
        App app = api.getApp(appId);
        
        downloader.download(app, session.getAuthSubToken());
    }
}
