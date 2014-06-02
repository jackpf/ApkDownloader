package com.jackpf.apkdownloader.Entity;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class ApkFile
{
    /**
     * File
     */
    private File file;
    
    /**
     * Icon
     */
    private Bitmap icon;
    
    /**
     * Constructor
     * 
     * @param context
     * @param file
     */
    public ApkFile(Context context, File file)
    {
        this.file = file;
        
        String filePath = file.getPath();
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        
        if(packageInfo != null) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            
            if (Build.VERSION.SDK_INT >= 8) {
                appInfo.sourceDir = filePath;
                appInfo.publicSourceDir = filePath;
            }
            
            Drawable icon = appInfo.loadIcon(context.getPackageManager());
            this.icon = ((BitmapDrawable) icon).getBitmap();
        }
    }
    
    /**
     * Get file
     * 
     * @return
     */
    public File getFile()
    {
        return file;
    }
    
    /**
     * Get icon
     * 
     * @return
     */
    public Bitmap getIcon()
    {
        return icon;
    }
}
