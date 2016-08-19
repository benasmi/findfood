package com.mabe.productions.findfood;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.mabe.productions.findfood.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ProfileActivity extends AppCompatActivity {

    //User info
    private EditText mail;
    private EditText phone_number;
    private EditText description;
    private EditText truck_names;
    private EditText monday;
    private EditText tuesday;
    private EditText wednesday;
    private EditText thursday;
    private EditText friday;
    private EditText saturday;
    private EditText sunday;
    private EditText slogan;
    private ImageView background_photo;
    private EditText special_offers;
    private String intent_truck_name;
    private EditText menu;

    //User coords
    private double myLongtitude;
    private double myLatitude;

    //Destination coords
    private double destinationLongtitude;
    private double destinationLatitude;

    private Toolbar myToolbar;
    private ImageView find_truck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        intent_truck_name = getIntent().getExtras().getString("username");
        special_offers = (EditText) findViewById(R.id.special_offers);
        mail = (EditText) findViewById(R.id.profile_mail);
        description = (EditText) findViewById(R.id.description);
        phone_number = (EditText) findViewById(R.id.number);
        truck_names = (EditText) findViewById(R.id.truck_name);
        slogan = (EditText) findViewById(R.id.slogan);
        monday = (EditText) findViewById(R.id.monday);
        tuesday = (EditText) findViewById(R.id.tuesday);
        wednesday = (EditText) findViewById(R.id.wednesday);
        thursday = (EditText) findViewById(R.id.thursday);
        friday = (EditText) findViewById(R.id.friday);
        saturday = (EditText) findViewById(R.id.saturday);
        sunday = (EditText) findViewById(R.id.sunday);
        background_photo = (ImageView) findViewById(R.id.background_picture);
        menu = (EditText) findViewById(R.id.menu);
        find_truck = (ImageView) findViewById(R.id.find_truck);

        new FetchUserData(intent_truck_name).execute();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLongtitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude();
        myLatitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude();


        myToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
        setSupportActionBar(myToolbar);

        find_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locate();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_for_user, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                CheckingUtils.dialogBoxForReport("Do you really want to report this truck?", ProfileActivity.this, intent_truck_name);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    public void locate() {


        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (enabled) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + myLatitude + "," + myLongtitude + "&daddr=" + destinationLatitude + "," + destinationLongtitude));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        } else {
            CheckingUtils.buildAlertMessageNoGps("You need GPS to get path directions, do you want to turn it on?", ProfileActivity.this);
        }


    }


    class FetchUserData extends AsyncTask<Void, Void, Void> {

        private Bitmap profile_pic;
        private ProgressDialog progressDialog;
        private String truck_name;
        private JSONObject userInfo;
        private JSONArray jsonArray;
        private JSONObject userSchedule;

        public FetchUserData(String truck_name) {
            this.truck_name = truck_name;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Getting information...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }


        public Bitmap getBitmapFromURL(String src) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://64.137.182.232/fetchProfileInfoForClient.php");


                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("truck_name", truck_name);


                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());

                jsonArray = new JSONArray(responseBody);

                userInfo = jsonArray.getJSONObject(0);
                userSchedule = jsonArray.getJSONObject(1);


                String link_background_photo = "http://64.137.182.232/pictures/" + userInfo.getString("username") + "." + userInfo.getString("ext_profile");
                profile_pic = getBitmapFromURL(link_background_photo);


                Log.i("TEST", String.valueOf(profile_pic));
                Log.i("TEST", this.truck_name);

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            try {
                //GENERAL info
                mail.setText(userInfo.getString("mail"));
                phone_number.setText(userInfo.getString("phone_number"));
                description.setText(userInfo.getString("description"));
                slogan.setText(userInfo.getString("slogan"));
                truck_names.setText(userInfo.getString("truck_name"));
                special_offers.setText(userInfo.getString("special_offer"));
                menu.setText(userInfo.getString("menu"));
                background_photo.setImageBitmap(profile_pic);


                destinationLongtitude = userInfo.getDouble("longtitude");
                destinationLatitude = userInfo.getDouble("latitude");

                //SCHEDULE info
                monday.setText(userSchedule.getString("monday"));
                tuesday.setText(userSchedule.getString("tuesday"));
                wednesday.setText(userSchedule.getString("wednesday"));
                thursday.setText(userSchedule.getString("thursday"));
                friday.setText(userSchedule.getString("friday"));
                saturday.setText(userSchedule.getString("saturday"));
                sunday.setText(userSchedule.getString("sunday"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(aVoid);
        }
    }


}