package com.jackpf.apkdownloader.UI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.jackpf.apkdownloader.Downloader;
import com.jackpf.apkdownloader.R;
import com.jackpf.apkdownloader.Exception.AuthenticationException;
import com.jackpf.apkdownloader.Exception.PlayApiException;
import com.jackpf.apkdownloader.Model.UIInterface;

public class MainActivityUI extends UIInterface
{
    private Activity activity;
    
    private ArrayAdapter<ArrayList<File>> adapter;
    
    private ArrayList<File> downloads;
    
    public MainActivityUI(Context context)
    {
        super(context);
        
        activity = (SherlockActivity) context;
    }
    
    public void initialise(Object ...params)
    {
        if (params[0] instanceof String) {
            ((EditText) activity.findViewById(R.id.app_id)).setText((String) params[0]);
        }
        
        ListView downloadsList = (ListView) activity.findViewById(R.id.downloads);
        
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Downloader.DOWNLOAD_DIR);
        if (dir.listFiles() != null) {
            downloads = new ArrayList<File>(Arrays.asList(dir.listFiles()));
        } else {
            downloads = new ArrayList<File>();
        }
        
        //if (adapter == null) {
            adapter = new ArrayAdapter<ArrayList<File>>(context, downloads);
            downloadsList.setAdapter(adapter);
            
            downloadsList.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    File file = (File) adapter.getItem(position);
                    
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        //} else {
            adapter.notifyDataSetChanged();
        //}
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
    
    private class ArrayAdapter<T extends List<?>> extends BaseAdapter
    {
        private final Context context;
        private final T objects;
        private final LayoutInflater inflater;

        public ArrayAdapter(Context context, T objects)
        {
            this.context = context;
            this.objects = objects;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public Object getItem(int position)
        {
            return objects.get(position);
        }

        public long getItemId(int position)
        {
            return position;
        }

        public int getCount()
        {
            return objects.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row;

            if (convertView == null) {
                row = inflater.inflate(R.layout._download_item, parent, false);
            } else {
                row = convertView;
            }
            
            File file = (File) getItem(position);
            
            ((TextView) row.findViewById(R.id.package_name)).setText(file.getName());

            if (file.getPath().endsWith(".apk")) {
                String filePath = file.getPath();
                PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
                
                if(packageInfo != null) {
                    ApplicationInfo appInfo = packageInfo.applicationInfo;
                    
                    if (Build.VERSION.SDK_INT >= 8) {
                        appInfo.sourceDir = filePath;
                        appInfo.publicSourceDir = filePath;
                    }
                    
                    Drawable icon = appInfo.loadIcon(context.getPackageManager());
                    Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();
                    
                    ((ImageView) row.findViewById(R.id.package_icon)).setImageBitmap(bmpIcon);
                }
            }
            
            return row;
        }
    }
}
