package com.mabe.productions.findfood;

import android.view.View;
import android.widget.TextView;

import com.mabe.productions.findfood.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Benas on 5/26/2016.
 */
public class InfoWindowClass implements GoogleMap.InfoWindowAdapter {

    private View view;

    public InfoWindowClass(View view) {
        this.view = view;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;

    }

    @Override
    public View getInfoWindow(Marker marker) {

        View view1 = view;

        TextView truck_name = (TextView) view1.findViewById(R.id.truck_name_marker);
        TextView slogan = (TextView) view1.findViewById(R.id.slogan_marker);

        truck_name.setText(marker.getTitle());
        slogan.setText(marker.getSnippet());

        return view1;
    }
}
