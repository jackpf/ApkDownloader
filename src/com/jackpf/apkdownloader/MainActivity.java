package com.jackpf.apkdownloader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.jackpf.apkdownloader.Request.TestRequest;
import com.jackpf.apkdownloader.UI.MainActivityUI;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new NetworkThread(this, new TestRequest(), new MainActivityUI(this)).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
