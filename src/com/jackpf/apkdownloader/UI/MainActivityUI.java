package com.jackpf.apkdownloader.UI;

import android.content.Context;
import android.widget.Toast;

import com.jackpf.apkdownloader.R;
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
        Toast.makeText(context.getApplicationContext(), context.getString(R.string.error_unrecognized_login), Toast.LENGTH_LONG).show();
    }
}
