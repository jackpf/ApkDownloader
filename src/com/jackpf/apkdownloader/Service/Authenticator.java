package com.jackpf.apkdownloader.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

import com.jackpf.apkdownloader.R;
import com.jackpf.apkdownloader.Exception.AuthenticationException;

public class Authenticator
{
    /**
     * Context
     */
    private Context context;
    
    /**
     * Auth sub-token
     */
    private static String subToken;
    
    /**
     * Preferences instance
     */
    private SharedPreferences prefs;
    
    /**
     * Google login credentials
     */
    private String email, password;
    
    /**
     * Auth service to get token for
     */
    private final String AUTH_SERVICE       = "androidsecure";
    
    /**
     * Auth account type
     */
    private final String AUTH_ACCOUNT_TYPE  = "HOSTED_OR_GOOGLE";
    
    /**
     * Client login url
     */
    private final String AUTH_URL           = "https://www.google.com/accounts/ClientLogin";
    
    /**
     * Constructor
     * 
     * @param context
     */
    public Authenticator(Context context)
    {
        this.context = context;
        
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        email       = prefs.getString(context.getString(R.string.pref_email_key), context.getString(R.string.pref_email_default));
        password    = prefs.getString(context.getString(R.string.pref_password_key), context.getString(R.string.pref_password_default));
    }
    
    /**
     * Get authentication token
     * 
     * @throws AuthenticationException
     * @return
     */
    public String getToken() throws AuthenticationException
    {
        // Can't get this to work quite right, not sure if it's not getting the right token
        // or it's because it's a different length, but the download request 400's with this one
        /*try {
            AccountManager am = AccountManager.get(context);
            Account[] accounts = am.getAccountsByType("com.google");
            AccountManagerFuture<Bundle> accountManagerFuture;
            accountManagerFuture = am.getAuthToken(accounts[0], "androidsecure", null, (Activity) context, null, null);
            Bundle authTokenBundle = accountManagerFuture.getResult();
            subToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
        if (subToken == null) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                
                params.add(new BasicNameValuePair("service", AUTH_SERVICE));
                params.add(new BasicNameValuePair("accountType", AUTH_ACCOUNT_TYPE));
                params.add(new BasicNameValuePair("Email", email));
                params.add(new BasicNameValuePair("Passwd", password));
                
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(AUTH_URL);
                
                post.addHeader("Content-Type", "application/x-www-form-urlencoded");
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                
                HttpResponse response = client.execute(post);
                
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new AuthenticationException(String.format("Login responded with status code %d", response.getStatusLine().getStatusCode()));
                }
                
                String data = EntityUtils.toString(response.getEntity());
        
                StringTokenizer st = new StringTokenizer(data, "\n\r=");
                
                while (st.hasMoreTokens()) {
                    if (st.nextToken().equalsIgnoreCase("Auth")) {
                        subToken = st.nextToken();
                        break;
                    }
                }
                
                if (subToken == null) {
                    throw new AuthenticationException("Auth key not found");
                }
            } catch (Exception e) {
                throw new AuthenticationException(e.getMessage(), e);
            }
        }
        
        return subToken;
    }
    
    /**
     * Get android id
     * 
     * @return
     */
    public String getAndroidId()
    {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }
    
    /**
     * Get google services framework id
     * 
     * @return
     */
    public String getGsfId() 
    {
        // Return preference if set
        String gsfid = prefs.getString(context.getString(R.string.pref_gsfid_key), context.getString(R.string.pref_gsfid_default));
        
        if (!gsfid.equals("")) {
            return gsfid;
        }
        
        // Otherwise attempt to get it from google services
        Cursor c = context.getContentResolver().query(
            Uri.parse("content://com.google.android.gsf.gservices"),
            null,
            null,
            new String[]{"android_id"},
            null
        );
        
        if (c == null || !c.moveToFirst() || c.getColumnCount() < 2) {
            return null;
        }
        
        try {
            return Long.toHexString(Long.parseLong(c.getString(1)));
        } catch (NumberFormatException e) {
            return null;
        } finally {
            c.close();
        }
    }
}
