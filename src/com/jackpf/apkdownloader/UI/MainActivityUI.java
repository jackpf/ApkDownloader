package com.jackpf.apkdownloader.UI;

import android.content.Context;
import android.widget.Toast;

import com.jackpf.apkdownloader.R;
import com.jackpf.apkdownloader.Exception.AuthenticationException;
import com.jackpf.apkdownloader.Exception.PlayApiException;
import com.jackpf.apkdownloader.Model.UIInterface;

public class MainActivityUI extends UIInterface
{
    public MainActivityUI(Context context)
    {
        super(context);
    }
    
    public void initialise()
    {
        
    }
    
    public void preUpdate()
    {
        
    }
    
    public void update()
    {
        
    }
    
    public void error(Exception e)
    {
        if (e instanceof AuthenticationException) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.error_unrecognized_login, e.getMessage()), Toast.LENGTH_LONG).show();
        } else if (e instanceof PlayApiException) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.error_api_exception, e.getMessage()), Toast.LENGTH_LONG).show();
        } else {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
