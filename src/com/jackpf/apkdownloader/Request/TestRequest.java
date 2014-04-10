package com.jackpf.apkdownloader.Request;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Base64;

import com.jackpf.apkdownloader.Entity.Response;
import com.jackpf.apkdownloader.Model.RequestInterface;
import com.jackpf.apkdownloader.Service.Serializer;

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
        /*final MarketSession session = new MarketSession(true);
        session.login("jack.philip.farrelly@gmail.com", "glhx xpow gkck lpks", "129596a2a13cb718");

        InstallAsset ia = session.queryGetAssetRequest("com.jackpf.sixpairtool").getInstallAsset(0);
        String cookieName = ia.getDownloadAuthCookieName();
        String cookieValue = ia.getDownloadAuthCookieValue();
        
        System.err.println("BLOB:"+ia.getBlobUrl());
        
        HttpClient a = new DefaultHttpClient();
        HttpPost request = new HttpPost("https://android.clients.google.com/market/api/ApiRequest");*/
        
        byte[]
            SEP_1 = new byte[]{16},
            SEP_2 = new byte[]{24},
            SEP_3 = new byte[]{34},
            SEP_4 = new byte[]{42},
            SEP_5 = new byte[]{50},
            SEP_6 = new byte[]{58},
            SEP_7 = new byte[]{66},
            SEP_8 = new byte[]{74},
            SEP_9 = new byte[]{82},
            SEP_10 = new byte[]{90},
            SEP_11 = new byte[]{19, 82},
            SEP_12 = new byte[]{10},
            SEP_13 = new byte[]{20},
            SEP_14 = new byte[]{10},
            SEP_15 = new byte[]{10}
        ;
        
        Serializer serializer = new Serializer();
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("authToken", "DQAAAPcAAAD3nw9whRZztwHkhUnxWe_UZbi7DXDM3rqiGrPagbBMgb3e0DxZyqLNwME6Idl5C05EA0qxVaLHayXhhvchai_i4TrEaikTuBDTMVr8-fOCRD9iXFgzUeQyMPBfzG0DCMUJW7LH7fW07LPGWGKmoCmhVnL-AOUfNlWiav6ZsmnyssYiTYVvJbe2RqeQsur79dEzVWo6ph5iAGxEMccMLcpaghlJf_6MKrzDDZY3UEMhtyoCpDz1QytXVNZj60sU0H6tpTDTxyoKgY6ED4e74TuDNSV8X6m-HEiUqO1W6-2ExThpiBFuLjsNTuM_TpTywLcAqpAo782CeA4KxHK6nRPX");
        map.put("SEP_1", SEP_1);
        map.put("isSecure", true);
        map.put("SEP_2", SEP_2);
        map.put("sdkVersion", 8013013);
        map.put("SEP_3", SEP_3);
        map.put("deviceId", "3b718fcb3fa05cba");
        map.put("SEP_4", SEP_4);
        map.put("deviceAndSdkVersion", "mako:18");
        map.put("SEP_5", SEP_5);
        map.put("locale", "en");
        map.put("SEP_6", SEP_6);
        map.put("country", "us");
        map.put("SEP_7", SEP_7);
        map.put("operatorAlpha", "T-Mobile");
        map.put("SEP_8", SEP_8);
        map.put("simOperatorAlpha", "T-Mobile");
        map.put("SEP_9", SEP_9);
        map.put("operatorNumeric", "31020");
        map.put("SEP_10", SEP_10);
        map.put("simOperatorNumeric", "31020");
        map.put("SEP_11", SEP_11);
        map.put("packageNameLength", "com.jackpf.blockchainsearch".length() + 2);
        map.put("SEP_12", SEP_12);
        map.put("packageName", "com.jackpf.blockchainsearch");
        map.put("SEP_13", SEP_13);
        map.put("SEP_14", SEP_14);
        Serializer.Bytes tmp = new Serializer.Bytes();
        int num = getSimOperatorLength(map);
        for (int times = 0; times < 5; times++) {
            int elm = num % 128;
            if ((num >>>= 7) > 0) {
                elm += 128;
            }
            tmp.add((byte) elm);
            if (num == 0) {
                break;
            }
        }
        byte[] tmp2 = new byte[tmp.size()];
        for (int j = 0; j < tmp.size(); j++) {
            tmp2[j] = tmp.get(j);
        }
        map.put("simOperatorLength", tmp2);
        map.put("SEP_15", SEP_15);
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            serializer.serialize(entry.getValue());
        }
        
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://android.clients.google.com/market/api/ApiRequest");
        
        byte[] realBytes = new byte[serializer.getBytes().size()];
        for (int i = 0; i < serializer.getBytes().size(); i++) {
            realBytes[i] = serializer.getBytes().get(i);
        }
        
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new StringEntity("version=2&request=" + Base64.encodeToString(realBytes, Base64.DEFAULT)));
        
        HttpResponse response = client.execute(post);
        System.err.println(response.getStatusLine().getStatusCode());
        
        StringWriter writer = new StringWriter();
        IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
        System.err.println(writer.toString());

    }
    
    private int getSimOperatorLength(Map<String, Object> map)
    {
        Serializer tmpSerializer = new Serializer();
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            tmpSerializer.serialize(entry.getValue());
            
            if (entry.getKey().equals("simOperatorNumeric")) {
                return tmpSerializer.getBytes().size() + 1;
            }
        }
        
        throw new RuntimeException("Unable to determine sim operator length");
    }
}
