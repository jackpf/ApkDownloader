package com.jackpf.apkdownloader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
