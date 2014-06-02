package com.jackpf.apkdownloader.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Base64;

import com.google.protobuf.ByteString;
import com.jackpf.apkdownloader.Entity.App;
import com.jackpf.apkdownloader.Exception.AuthenticationException;
import com.jackpf.apkdownloader.Exception.PlayApiException;
import com.jackpf.apkdownloader.Proto.Play;

public class PlayApi
{
    /**
     * Authenticator
     */
    private Authenticator authenticator;
    
    /**
     * Request vars
     */
    private final int       SDK_VERSION            = 8013013;
    private final String    DEVICE_AND_SDK_VERSION = "mako:18";
    private final String    OPERATOR               = "T-Mobile";
    private final String    OPERATOR_NUMERIC       = "31020";
    private final String    LOCALE                 = "en";
    private final String    COUNTRY                = "us";
    
    /**
     * Request url
     */
    private final String    REQUEST_URL            = "https://android.clients.google.com/market/api/ApiRequest";
    /**
     * Request version
     */
    private final int       REQUEST_VERSION        = 2;
    
    /**
     * Constructor
     * 
     * @param authenticator
     */
    public PlayApi(Authenticator authenticator)
    {
        this.authenticator = authenticator;
    }
    
    /**
     * Get app by package name
     * 
     * @param packageName
     * @return
     * @throws PlayApiException
     */
    public App getApp(String packageName) throws PlayApiException, AuthenticationException
    {
        byte[] protoBuf = buildProtoBuf(packageName);
        
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(REQUEST_URL);
            
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(
                new StringEntity(String.format(
                    "version=%d&request=%s",
                    REQUEST_VERSION,
                    Base64.encodeToString(protoBuf, Base64.DEFAULT)
                ))
            );
            
            HttpResponse response = client.execute(post);
            
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new PlayApiException(String.format("Server responded with status code %d", response.getStatusLine().getStatusCode()));
            }
    
            byte[] bin = EntityUtils.toByteArray(response.getEntity());
            
            ByteArrayInputStream bais   = new ByteArrayInputStream(bin);
            GZIPInputStream gzis        = new GZIPInputStream(bais);
            InputStreamReader reader    = new InputStreamReader(gzis);
            BufferedReader in           = new BufferedReader(reader);
    
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            
            return new App(packageName, extractDownloadPath(sb.toString()), extractMarketDA(sb.toString()));
        } catch (Exception e) {
            throw new PlayApiException(e.getMessage());
        }
    }
    
    /**
     * Manually build a protobuf request
     * 
     * @param packageName
     * @return
     */
    private byte[] buildProtoBuf(String packageName) throws AuthenticationException
    {
        // Generate byte array from the auth subtoken
        byte[]
            authTokenBytes  = authenticator.getToken().getBytes(),
            authExtraBytes  = {16, 1}, // Not sure why these have to be appended, but hey ho
            authBytes       = new byte[authTokenBytes.length + authExtraBytes.length]
        ;

        System.arraycopy(authTokenBytes, 0, authBytes, 0, authTokenBytes.length);
        System.arraycopy(authExtraBytes, 0, authBytes, authTokenBytes.length, authExtraBytes.length);
        
        // Build a (mostly correct) request protobuf
        Play.RequestContext proto = Play.RequestContext.newBuilder().addApp(
            Play.RequestContext.newBuilder().addAppBuilder()
                .setAuthSubToken(ByteString.copyFrom(authBytes))
                .setVersion(SDK_VERSION)
                .setAndroidId(authenticator.getGsfId())
                .setDeviceAndSdkVersion(DEVICE_AND_SDK_VERSION)
                .setUserLanguage(LOCALE)
                .setUserCountry(COUNTRY)
                .setOperatorAlpha(OPERATOR)
                .setSimOperatorAlpha(OPERATOR)
                .setOperatorNumeric(OPERATOR_NUMERIC)
                .setSimOperatorNumeric(OPERATOR_NUMERIC)
        ).build();
        
        byte partialProtoBytes[] = proto.toByteArray();
        partialProtoBytes[4] = -53; // Ahem...
        
        ArrayList<Byte> packageNameByteList = new ArrayList<Byte>();
        packageNameByteList.add((byte) 19);
        packageNameByteList.add((byte) 82);
        packageNameByteList.add((byte) 29);
        packageNameByteList.add((byte) 10);
        packageNameByteList.add((byte) packageName.length());
        for (int i = 0; i < packageName.length(); i++) {
            packageNameByteList.add((byte) packageName.charAt(i));
        }
        packageNameByteList.add((byte) 20);
        
        byte[] packageNameBytes = new byte[packageNameByteList.size()];
        for (int i = 0; i < packageNameByteList.size(); i++) {
            packageNameBytes[i] = packageNameByteList.get(i);
        }
        
        byte protoBytes[] = new byte[partialProtoBytes.length + packageNameBytes.length];
        System.arraycopy(partialProtoBytes, 0, protoBytes, 0, partialProtoBytes.length);
        System.arraycopy(packageNameBytes, 0, protoBytes, partialProtoBytes.length, packageNameBytes.length);
        
        return protoBytes;
    }
    
    /**
     * Manually extract the download path from the protobuf response
     * 
     * @param str
     * @return
     * @throws PlayApiException
     */
    private String extractDownloadPath(String str) throws PlayApiException
    {
        Pattern p = Pattern.compile("(?i)https?://[^:]+");
        Matcher m = p.matcher(str);
        
        if (!m.find()) {
            throw new PlayApiException("App not found");
        }
        
        return m.group(0);
    }
    
    /**
     * Manually extract the market da from the protobuf response
     * 
     * @param str
     * @return
     */
    private String extractMarketDA(String str)
    {
        boolean capture = false;
        StringBuilder sb = new StringBuilder();
        
        for (int i = str.lastIndexOf(0x014); i < str.length(); i++) {
            byte b = (byte) str.charAt(i);
            if (b == 0x014) {
                capture = true;
            } else if (capture && b == 0x0c) {
                break;
            } else if (capture) {
                sb.append(str.charAt(i));
            }
        }
        
        return sb.toString();
    }
}
