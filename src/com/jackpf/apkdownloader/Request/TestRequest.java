package com.jackpf.apkdownloader.Request;

import com.gc.android.market.api.MarketSession;
import com.jackpf.apkdownloader.Entity.Response;
import com.jackpf.apkdownloader.Model.RequestInterface;
import com.jackpf.apkdownloader.Service.PlayApi;

public class TestRequest extends RequestInterface
{
    @Override
    public Response call(Object ...params)
    {
        try {
        search();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void search() throws Exception
    {
        MarketSession session = new MarketSession(true);
        session.login("jack.philip.farrelly@gmail.com", "bjfg yjdr giqc pvkn", "129596a2a13cb718");
        
        PlayApi api = new PlayApi(session.getAuthSubToken(), "3b718fcb3fa05cba");
        System.err.println(api.getDownloadPath("com.jackpf.blockchainsearch"));
    }
}
