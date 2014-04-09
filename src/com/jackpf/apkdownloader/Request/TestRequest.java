package com.jackpf.apkdownloader.Request;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.GetAssetResponse.InstallAsset;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.jackpf.apkdownloader.Entity.Response;
import com.jackpf.apkdownloader.Model.RequestInterface;

public class TestRequest extends RequestInterface
{
    @Override
    public Response call(Object ...params)
    {
        search();
        
        return null;
    }
    
    private void download()
    {
        
    }
    
    private void search()
    {System.err.println("call");
        final MarketSession session = new MarketSession(true);
        session.login("jack.philip.farrelly@gmail.com", "glhx xpow gkck lpks", "129596a2a13cb718");

        InstallAsset ia = session.queryGetAssetRequest("com.jackpf.sixpairtool").getInstallAsset(0);
        String cookieName = ia.getDownloadAuthCookieName();
        String cookieValue = ia.getDownloadAuthCookieValue();
        
        System.err.println("BLOB:"+ia.getBlobUrl())
    }
}
