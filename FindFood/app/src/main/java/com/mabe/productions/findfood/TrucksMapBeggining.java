package com.mabe.productions.findfood;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mabe.productions.findfood.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrucksMapBeggining extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private double longtitude;
    private double latitude;
    private JSONArray array;
    private boolean isFinished = false;
    private ImageView refresh_image;
    private double myLongtitude;
    private double myLatitude;
    private ArrayList<InfoHolder> markerList = new ArrayList<InfoHolder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucks_map_beggining);

        refresh_image = (ImageView) findViewById(R.id.refresh_map1);

        refresh_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCoords();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setMyLocationEnabled(true);

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                if (CheckingUtils.isGpsEnabled(TrucksMapBeggining.this)) {
                    return false;
                } else {
                    CheckingUtils.buildAlertMessageNoGps("You need GPS to do it, do you want to turn it on?", TrucksMapBeggining.this);
                }
                return false;


            }
        });
        if (mGoogleMap != null) {
            View view = getLayoutInflater().inflate(R.layout.marker_info, null);
            mGoogleMap.setInfoWindowAdapter(new InfoWindowClass(view));


        }
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (!CheckingUtils.isGpsEnabled(TrucksMapBeggining.this)) {
                    CheckingUtils.buildAlertMessageNoGps("You need to enable your GPS to visit profile, do you want to enable it ?", TrucksMapBeggining.this);
                    return;
                }

                if (!CheckingUtils.isNetworkConnected(TrucksMapBeggining.this)) {
                    CheckingUtils.createErrorBox("You need internet connection to do that", TrucksMapBeggining.this);
                    return;
                } else {
                    Intent i = new Intent(TrucksMapBeggining.this, ProfileActivity.class);
                    for (int z = 0; z < markerList.size(); z++) {
                        String truck = markerList.get(z).getMarker().getTitle();
                        if (marker.getTitle().equals(truck)) {
                            i.putExtra("username", markerList.get(z).getUsername());
                            break;
                        }

                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);


                    return;
                }
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 16));
        }

        Log.i("TEST", String.valueOf(myLongtitude));

        new fetcher().execute();

    }
    public void updateCoords() {
        markerList.clear();
        mGoogleMap.clear();
        new fetcher().execute();
    }

    class fetcher extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://64.137.182.232/fetchCoordinates.php");

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());

                //Trying to get fetch from array.

                array = new JSONArray(responseBody);

                isFinished = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                if (isFinished) {


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        if (!object.isNull("longtitude")) {
                            latitude = object.getDouble("latitude");
                            longtitude = object.getDouble("longtitude");
                            final LatLng truckCoords = new LatLng(latitude, longtitude);
                            final String username = object.getString("username");
                            final String truck_name = object.getString("truck_name");
                            final String slogan = object.getString("slogan");
                            final int marker_icon = Integer.parseInt(object.getString("marker_icon"));
                            Log.i("TEST", String.valueOf(marker_icon));

                            switch (marker_icon) {
                                case 0:
                                    markerList.add(new InfoHolder(new MarkerOptions().position(truckCoords).title(truck_name).snippet(slogan).icon(BitmapDescriptorFactory.fromResource(R.drawable.butcher_marker)), username, 0));
                                    break;

                                case 1:
                                    markerList.add(new InfoHolder(new MarkerOptions().position(truckCoords).title(truck_name).snippet(slogan).icon(BitmapDescriptorFactory.fromResource(R.drawable.sandwich_marker)), username, 0));
                                    break;

                                case 2:
                                    markerList.add(new InfoHolder(new MarkerOptions().position(truckCoords).title(truck_name).snippet(slogan).icon(BitmapDescriptorFactory.fromResource(R.drawable.burger_marker)), username, 0));
                                    break;

                                case 3:
                                    markerList.add(new InfoHolder(new MarkerOptions().position(truckCoords).title(truck_name).snippet(slogan).icon(BitmapDescriptorFactory.fromResource(R.drawable.candy_marker)), username, 0));
                                    break;

                                case 4:
                                    markerList.add(new InfoHolder(new MarkerOptions().position(truckCoords).title(truck_name).snippet(slogan).icon(BitmapDescriptorFactory.fromResource(R.drawable.drink_marker)), username, 0));
                                    break;
                            }


                            mGoogleMap.addMarker(markerList.get(i).getMarker());
                            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    marker.showInfoWindow();

                                    return true;
                                }
                            });
                        }


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
