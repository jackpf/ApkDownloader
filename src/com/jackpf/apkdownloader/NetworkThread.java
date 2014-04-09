package com.jackpf.apkdownloader;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;

import com.jackpf.apkdownloader.Model.RequestInterface;
import com.jackpf.apkdownloader.Model.UIInterface;

/**
 * Network thread
 * Performs all api requests
 */
public class NetworkThread extends AsyncTask<Object, Void, Void>
{
    /**
     * Context called from
     */
    // Not really needed
    //private Context context;
    
    /**
     * Api request to call
     */
    private RequestInterface request;
    
    /**
     * UI to pass data to
     */
    private UIInterface ui;
    
    /**
     * UI vars
     */
    HashMap<String, Object> vars = new HashMap<String, Object>();
    
    /**
     * Exception caused in the network thread
     */
    Exception e = null;
    
    /**
     * Constructor
     * 
     * @param context
     * @param request
     * @param ui
     */
    public NetworkThread(Context context, RequestInterface request, UIInterface ui)
    {
        //this.context = context;
        this.request = request;
        this.ui = ui;
    }

    /**
     * Thread pre execute
     * Just call the UI's preExecute method
     */
    @Override
    protected void onPreExecute()
    {
        ui.preUpdate();
    }

    /**
     * Perform request
     * 
     * @param params
     */
    @Override
    protected Void doInBackground(Object... params)
    {
        try {
            // Just set the vars to the response
            vars = request.call(params);
        } catch (Exception e) {
            e.printStackTrace();
            this.e = e;
        }

        return null;
    }
    
    /**
     * Post execute
     * Pass vars to UI
     * 
     * @param _void
     */
    @Override
    protected void onPostExecute(Void _void)
    {
        ui.setVars(vars);

        if (e == null) {
            ui.update();
        } else {
            ui.error(e);
        }
    }
}