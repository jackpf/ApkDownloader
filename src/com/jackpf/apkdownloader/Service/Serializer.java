package com.jackpf.apkdownloader.Service;

import java.util.ArrayList;

public class Serializer
{
    /**
     * Bytes
     */
    private Bytes bytes;
    
    /**
     * Constructor
     */
    public Serializer()
    {
        bytes = new Bytes();
    }
    
    /**
     * String serializer
     * 
     * @param s
     * @return
     */
    public Bytes serialize(String s)
    {
        serialize(s.length());
        
        bytes.addAll(s.getBytes());
        
        return bytes;
    }
    
    /**
     * Int serializer
     * 
     * @param num
     * @return
     */
    public Bytes serialize(Integer num)
    {
        for (int times = 0; times < 5; times++) {
            int elm = num % 128;
            if ((num >>>= 7) > 0) {
                elm += 128;
            }
            bytes.add((byte) elm);
            if (num == 0) {
                break;
            }
        }
        
        return bytes;
    }
    
    /**
     * Boolean serializer
     * 
     * @param b
     * @return
     */
    public Bytes serialize(Boolean b)
    {
        bytes.add((byte) (b ? 1 : 0));
        
        return bytes;
    }
    
    /**
     * Raw bytes serializer
     * 
     * @param array
     * @return
     */
    public Bytes serialize(byte[] array)
    {
        bytes.addAll(array);
        
        return bytes;
    }
    
    /**
     * Object serializer
     * 
     * @param o
     * @return
     */
    public Bytes serialize(Object o)
    {
        if (o instanceof String) {
            return serialize((String) o);
        } else if (o instanceof Integer) {
            return serialize((Integer) o);
        } else if (o instanceof Boolean) {
            return serialize((Boolean) o);
        } else if (o instanceof byte[]) {
            return serialize((byte[]) o);
        } else {
            throw new RuntimeException(String.format("Invalid type of %s", o.getClass().getName()));
        }
    }
    
    /**
     * Get bytes
     * 
     * @return
     */
    public Bytes getBytes()
    {
        return bytes;
    }
    
    /**
     * Bytes entity
     */
    public static class Bytes extends ArrayList<Byte>
    {
        /**
         * Generated UID
         */
        private static final long serialVersionUID = 3794239392551729969L;
        
        public void addAll(byte[] array)
        {
            for (int i = 0; i < array.length; i++) {
                add(array[i]);
            }
        }
    }
}
