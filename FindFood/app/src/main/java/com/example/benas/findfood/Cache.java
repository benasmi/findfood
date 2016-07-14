package com.example.benas.findfood;

import android.graphics.Bitmap;
public class Cache extends android.util.LruCache<String,Bitmap>{

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        return super.sizeOf(key, bitmap);
    }

    public Cache(int maxSize) {
        super(maxSize);
    }

    public void saveToMemoryCache(String key, Bitmap bitmap){
        if(key==null){
            this.put(key, bitmap);
        }else{
            this.remove(key);
            this.put(key, bitmap);
        }
    }
    public Bitmap getFromMemoryCache(String key){
        return this.get(key);
    }


}