package com.jackpf.apkdownloader.Model;

import java.util.HashMap;

import android.content.Context;

/**
 * UI interface
 */
public abstract class UIInterface
{
    /**
     * Parent context
     */
    protected Context context;
    
    /**
     * UI vars
     */
    protected HashMap<String, Object> vars;
    
    /**
     * Constructor
     * 
     * @param context
     */
    public UIInterface(Context context)
    {
        this.context = context;
    }
    
    /**
     * Set vars
     * 
     * @param vars
     */
    public void setVars(HashMap<String, Object> vars)
    {
        this.vars = vars;
    }
    
    /**
     * Initialise UI
     * Should be called in activity onCreate
     */
    public abstract void initialise();
    
    /**
     * Called pre update from thread
     */
    public abstract void preUpdate();
    
    /**
     * Update ui
     */
    public abstract void update();
    
    /**
     * Render error
     * 
     * @param e
     */
    public abstract void error(Exception e);
}