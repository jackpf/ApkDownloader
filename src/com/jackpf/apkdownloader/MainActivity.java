package com.jackpf.apkdownloader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.jackpf.apkdownloader.Request.DownloadRequest;
import com.jackpf.apkdownloader.Service.Authenticator;
import com.jackpf.apkdownloader.UI.MainActivityUI;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void download(View view)
    {
        String appId = ((EditText) findViewById(R.id.app_id)).getText().toString();
        
        new NetworkThread(
            this,
            new DownloadRequest(),
            new MainActivityUI(this)
        ).execute(
            new Authenticator(this),
            new Downloader(this),
            appId
        );
    }
}
