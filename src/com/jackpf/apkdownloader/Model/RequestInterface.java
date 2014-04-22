package com.jackpf.apkdownloader.Model;

import java.security.InvalidParameterException;

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
     * Get a parameter for a given index and do some type checking
     * 
     * @param i
     * @param c
     * @return
     */
    protected Object getParam(int i, Class<?> c)
    {
        if (params.length < i + 1) {
            throw new InvalidParameterException("Not enough arguments");
        }
        
        Object o = params[i];
        
        if (!o.getClass().equals(c)) {
            throw new InvalidParameterException(String.format("Expected argument of type %s but got %s", o.getClass(), c));
        }
        
        return o;
    }
    
    /**
     * Perform api call
     * 
     * @return
     * @throws Exception
     */
    public abstract Response call(Object ...params) throws Exception;
}