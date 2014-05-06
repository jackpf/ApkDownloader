package com.jackpf.apkdownloader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jackpf.apkdownloader.Model.UIInterface;
import com.jackpf.apkdownloader.Request.DownloadRequest;
import com.jackpf.apkdownloader.Service.Authenticator;
import com.jackpf.apkdownloader.UI.MainActivityUI;

public class MainActivity extends SherlockActivity
{
    private UIInterface ui;
    private NetworkThread thread;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ui = new MainActivityUI(this);
        
        String appId = null;
        if (Intent.ACTION_VIEW.equals(getIntent().getAction()) && getIntent().getDataString() != null) {
            appId = extractPackageId(getIntent().getDataString());
        } else if(Intent.ACTION_SEND.equals(getIntent().getAction()) && getIntent().getExtras().containsKey(Intent.EXTRA_TEXT)) {
            appId = extractPackageId(getIntent().getExtras().getString(Intent.EXTRA_TEXT));
        }
        
        ui.initialise(appId);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        
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

    /**
     * Get the package id from a market url
     * 
     * @param path
     * @return
     */
    protected String extractPackageId(String path)
    {
        Pattern p = Pattern.compile("id=(.*?)($|&)");
        Matcher m = p.matcher(path);
        
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }
    
    /**
     * Download button click
     * 
     * @param view
     */
    public void download(View view)
    {
        String appId = ((EditText) findViewById(R.id.app_id)).getText().toString();
        
        if (!appId.equals("")) {
            if (thread instanceof NetworkThread) {
                thread.cancel(true);
            }
            
            thread = new NetworkThread(
                this,
                new DownloadRequest(),
                ui
            );
            thread.execute(
                new Authenticator(this),
                new Downloader(this, ui),
                appId
            );
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_empty_id), Toast.LENGTH_LONG).show();
        }
    }
}
