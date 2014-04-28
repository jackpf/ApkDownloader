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
import com.jackpf.apkdownloader.Request.DownloadRequest;
import com.jackpf.apkdownloader.Service.Authenticator;
import com.jackpf.apkdownloader.UI.MainActivityUI;

public class MainActivity extends SherlockActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (Intent.ACTION_VIEW.equals(getIntent().getAction()) && getIntent().getDataString() != null) {
            setAppId(extractPackageId(getIntent().getDataString()));
        } else if(Intent.ACTION_SEND.equals(getIntent().getAction()) && getIntent().getExtras().containsKey(Intent.EXTRA_TEXT)) {
            setAppId(extractPackageId(getIntent().getExtras().getString(Intent.EXTRA_TEXT)));
        }
    }

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
    
    protected void setAppId(String id)
    {
        ((EditText) findViewById(R.id.app_id)).setText(id);
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
    
    public void download(View view)
    {
        String appId = ((EditText) findViewById(R.id.app_id)).getText().toString();
        
        if (!appId.equals("")) {
            new NetworkThread(
                this,
                new DownloadRequest(),
                new MainActivityUI(this)
            ).execute(
                new Authenticator(this),
                new Downloader(this),
                appId
            );
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_empty_id), Toast.LENGTH_LONG).show();
        }
    }
}
