package com.jackpf.apkdownloader.Model;

import com.jackpf.apkdownloader.Entity.Response;


/**
 * Request interface
 */
public abstract class RequestInterface
{
    /**
     * Api call params
     */
    protected Object[] params;
    
    /**
     * Constructor
     * 
     * @param params
     */
    public RequestInterface(Object ...params)
    {
        this.params = params;
    }
    
    /**
     * Perform api call
     * 
     * @return
     * @throws Exception
     */
    public abstract Response call(Object ...params) throws Exception;
}