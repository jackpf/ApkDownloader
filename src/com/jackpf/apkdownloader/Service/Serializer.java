package com.jackpf.apkdownloader.Service;

import com.jackpf.apkdownloader.Entity.Bytes;

public class Serializer
{
    private Bytes bytes;
    
    public Serializer()
    {
        this.bytes = new Bytes();
    }
    
    public Bytes serialize(String s)
    {
        serialize(s.length());
        for (int i = 0; i < s.length(); i++) {
            bytes.add((int) s.charAt(i));
        }
        
        return bytes;
    }
    
    public Bytes serialize(Integer num)
    {
        for (int i = 0; i < 5; i++) {
            int elm = num % 128;
            if ((num >>>= 7) > 0) {
                elm += 128;
            }
            bytes.add(elm);
            if (num == 0) {
                break;
            }
        }
        
        return bytes;
    }
    
    public Bytes serialize(Boolean b)
    {
        bytes.add(b ? 1 : 0);
        
        return bytes;
    }
    
    public Bytes serialize(Bytes newBytes)
    {
        bytes.addAll(newBytes);
        
        return bytes;
    }
    
    public Bytes serialize(Object o)
    {
        if (o instanceof String) {
            return serialize((String) o);
        } else if (o instanceof Integer) {
            return serialize((Integer) o);
        } else if (o instanceof Boolean) {
            return serialize((Boolean) o);
        } else if (o instanceof Bytes) {
            return serialize((Bytes) o);
        } else {
            throw new RuntimeException(String.format("Invalid type of %s", o.getClass().getName()));
        }
    }
    
    public Bytes getBytes()
    {
        return bytes;
    }
}
