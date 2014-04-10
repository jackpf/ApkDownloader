package com.jackpf.apkdownloader.Request;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

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
    
    private void search()
    {System.err.println("call");
        /*final MarketSession session = new MarketSession(true);
        session.login("jack.philip.farrelly@gmail.com", "glhx xpow gkck lpks", "129596a2a13cb718");

        InstallAsset ia = session.queryGetAssetRequest("com.jackpf.sixpairtool").getInstallAsset(0);
        String cookieName = ia.getDownloadAuthCookieName();
        String cookieValue = ia.getDownloadAuthCookieValue();
        
        System.err.println("BLOB:"+ia.getBlobUrl());
        
        HttpClient a = new DefaultHttpClient();
        HttpPost request = new HttpPost("https://android.clients.google.com/market/api/ApiRequest");*/
        
        Bytes
            SEP_1 = B(16),
            SEP_2 = B(24),
            SEP_3 = B(34),
            SEP_4 = B(42),
            SEP_5 = B(50),
            SEP_6 = B(58),
            SEP_7 = B(66),
            SEP_8 = B(74),
            SEP_9 = B(82),
            SEP_10 = B(90),
            SEP_11 = B(19, 82),
            SEP_12 = B(10),
            SEP_13 = B(20),
            SEP_14 = B(10),
            SEP_15 = B(10)
        ;
        
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
        map.put("simOperatorLength", serialize(new Bytes(), getSimOperatorLength(map)));
        map.put("SEP_15", SEP_15);
        
        Bytes bytes = new Bytes();
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            serialize(bytes, entry.getValue());
            System.err.println(entry.getKey()+": "+bytes.size());
        }
        
        String payload = getEncoded(bytes);
    }
    
    private class Bytes extends ArrayList<Integer>
    {
        /**
         * 
         */
        private static final long serialVersionUID = 3794239392551729969L;
    }
    
    private Bytes B(int ...ints)
    {
        Bytes bytes = new Bytes();
        
        for (int i = 0; i < ints.length; i++) {
            bytes.add(ints[i]);
        }
        
        return bytes;
    }
    
    private Bytes serialize(Bytes bytes, String s)
    {
        serialize(bytes, s.length());
        for (int i = 0; i < s.length(); i++) {
            bytes.add((int) s.charAt(i));
        }
        
        return bytes;
    }
    
    private Bytes serialize(Bytes bytes, Integer num)
    {
        for (int i = 0; i < 5; i++) {
            int elm = num % 128;
            if ((num >>>= 7) > 0) {
                elm += 128;
            }
            bytes.add(elm);
            if (num == 0) {
                break;
            }
        }
        
        return bytes;
    }
    
    private Bytes serialize(Bytes bytes, Boolean b)
    {
        bytes.add(b ? 1 : 0);
        
        return bytes;
    }
    
    private Bytes serialize(Bytes bytes, Bytes newBytes)
    {
        bytes.addAll(newBytes);
        
        return bytes;
    }
    
    private Bytes serialize(Bytes bytes, Object o)
    {
        if (o instanceof String) {
            return serialize(bytes, (String) o);
        } else if (o instanceof Integer) {
            return serialize(bytes, (Integer) o);
        } else if (o instanceof Boolean) {
            return serialize(bytes, (Boolean) o);
        } else if (o instanceof Bytes) {
            return serialize(bytes, (Bytes) o);
        } else {
            throw new RuntimeException(String.format("Invalid type of %s", o.getClass().getName()));
        }
    }
    
    private String getEncoded(Bytes bytes)
    {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < bytes.size(); i++) {
            int prim = bytes.get(i);
            sb.append((char) prim);
        }
        
        return sb.toString();
    }
    
    private int getSimOperatorLength(Map<String, Object> map)
    {
        Bytes bytes = new Bytes();
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            serialize(bytes, entry.getValue());
            
            if (entry.getKey().equals("simOperatorNumeric")) {
                return bytes.size() + 1;
            }
        }
        
        throw new RuntimeException("Unable to determine sim operator length");
    }
}
