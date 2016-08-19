package com.mabe.productions.findfood;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Benas on 5/26/2016.
 */
public class InfoHolder {

    MarkerOptions marker;
    String username;
    int markerId;

    public InfoHolder(MarkerOptions marker, String username, int markerId) {
        this.marker = marker;
        this.username = username;
        this.markerId = markerId;
    }

    public String getUsername() {
        return username;
    }

    public MarkerOptions getMarker() {
        return marker;
    }

    public int getMarkerId() {
        return markerId;
    }

    public void setMarker(MarkerOptions marker) {
        this.marker = marker;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
