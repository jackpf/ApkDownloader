package com.jackpf.apkdownloader.Exception;

public class PlayApiException extends Exception
{
    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6692883201561526770L;

    public PlayApiException(String s)
    {
        super(s);
    }

    public PlayApiException(String s, Exception previous)
    {
        super(s, previous);
    }
}
