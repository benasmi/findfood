package com.example.benas.findfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;
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

public class AlreadyLoggedIn extends AppCompatActivity {



    private EditText profile_mail;
    private EditText profile_phone_number;
    private EditText profile_description;
    private EditText profile_truck_names;
    private EditText profile_monday;
    private EditText profile_tuesday;
    private EditText profile_wednesday;
    private EditText profile_thursday;
    private EditText profile_friday;
    private EditText profile_saturday;
    private EditText profile_sunday;
    private EditText profile_slogan;
    private ToggleButton is_working;
    private ImageView profile_background_photo;
    private ImageView profile_menu_photo;
    private int whichImage;
    private EditText profile_special_offers;
    private SharedPreferences sharedPreferences;
    private EditText profile_website;
    private ImageView profile_logout;

    private JSONArray jsonArrayExtBackground;
    private JSONObject jsonObjectExtBackground;

    private JSONArray jsonArrayExtMenu;
    private JSONObject jsonObjectExtMenu;


    //Markers pictures
    private ImageView butcher_marker;
    private ImageView sandwich_marker;
    private ImageView candy_marker;
    private ImageView drink_marker;
    private ImageView burger_marker;
    private int whichMarker;
    private String username;
    private String password;
    private Cache memoryCache;

    public AlreadyLoggedIn(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_logged_in);


        profile_special_offers = (EditText) findViewById(R.id.profile_special_offers);
        profile_mail = (EditText) findViewById(R.id.profile_mail);
        profile_description = (EditText) findViewById(R.id.profile_description);
        profile_phone_number = (EditText) findViewById(R.id.number);
        is_working = (ToggleButton) findViewById(R.id.profile_is_working);
        profile_truck_names = (EditText) findViewById(R.id.truck_name);
        profile_website = (EditText) findViewById(R.id.website);
        profile_slogan = (EditText) findViewById(R.id.profile_slogan);
        profile_monday = (EditText) findViewById(R.id.profile_monday);
        profile_tuesday = (EditText) findViewById(R.id.profile_tuesday);
        profile_wednesday = (EditText) findViewById(R.id.profile_wednesday);
        profile_thursday = (EditText) findViewById(R.id.profile_thursday);
        profile_friday = (EditText) findViewById(R.id.profile_friday);
        profile_saturday = (EditText) findViewById(R.id.profile_saturday);
        profile_sunday = (EditText) findViewById(R.id.profile_sunday);
        profile_menu_photo = (ImageView) findViewById(R.id.profile_menu_picture);
        profile_background_photo = (ImageView) findViewById(R.id.background_picture);
        profile_logout = (ImageView) findViewById(R.id.profile_logout);

        //420
        memoryCache = new Cache(((int)Runtime.getRuntime().maxMemory()/1024));

        //Markers
        butcher_marker = (ImageView) findViewById(R.id.butcher_marker);
        burger_marker = (ImageView) findViewById(R.id.burger_marker);
        candy_marker = (ImageView) findViewById(R.id.candy_marker);
        drink_marker = (ImageView) findViewById(R.id.drink_marker);
        sandwich_marker = (ImageView) findViewById(R.id.sandwitch_marker);


        sharedPreferences = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        password = sharedPreferences.getString("password", null);

        new FetchUserData(username).execute();


        profile_background_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkAvailable()) {
                    new AlertDialog.Builder(AlreadyLoggedIn.this)
                            .setMessage("You need internet connection to do that")
                            .setPositiveButton("UNDERSTOOD", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                }


            }
        });

        profile_menu_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isNetworkAvailable()){
                    new AlertDialog.Builder(AlreadyLoggedIn.this)
                            .setMessage("You need internet connection to do that")
                            .setPositiveButton("UNDERSTOOD", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {



                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 2);
                }

            }
        });

        profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = AlreadyLoggedIn.this.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

                new AlertDialog.Builder(AlreadyLoggedIn.this)
                        .setMessage("Do you want to logout?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", null);
                                editor.putString("password", null);
                                editor.putBoolean("rememberPassword", false);
                                editor.commit();

                                startActivity(new Intent(AlreadyLoggedIn.this, LoginOrRegister.class));
                                ((Activity) AlreadyLoggedIn.this).finish();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        is_working.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (profile_slogan.getText().toString().isEmpty() && isChecked){
//                    profile_slogan.setError("Create slogan to change work status");
//                    is_working.setChecked(false);
//                    return;
//                }
//                if (profile_truck_names.getText().toString().isEmpty() && isChecked){
//                    profile_truck_names.setError("Create truck name to change work status");
//                    is_working.setChecked(false);
//                    return;
//                }
                    if (!isNetworkAvailable()) {
                        is_working.setChecked(false);
                        new AlertDialog.Builder(AlreadyLoggedIn.this)
                                .setMessage("You need internet connection to do that")
                                .setPositiveButton("UNDERSTOOD", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        dialog.dismiss();
                                    }
                                })
                                .show();

                        return;
                    } else {
                        new AlertDialog.Builder(AlreadyLoggedIn.this)
                                .setMessage("Don't forget to change status, after you finish your work")
                                .setPositiveButton("UNDERSTOOD", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        new ServerManager(AlreadyLoggedIn.this, "CHANGE_IS_WORKING").execute("CHANGE_IS_WORKING", is_working.isChecked() ? "1" : "0", username, password);

                                        dialog.dismiss();
                                    }
                                })
                            .show();
                }





            }
        });




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);

                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("png")) {
                    final String username = sharedPreferences.getString("username", null);
                    final String password = sharedPreferences.getString("password", null);
                    new ServerManager(AlreadyLoggedIn.this, this, "UPDATE_PICTURE").execute("UPDATE_PICTURE", username, password, filePath);

                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
        }if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK) {


                Uri selectedImage = data.getData();
                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);

                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("png")) {
                    final String username = sharedPreferences.getString("username", null);
                    final String password = sharedPreferences.getString("password", null);
                    new ServerManager(AlreadyLoggedIn.this,this, "UPDATE_MENU_PICTURE").execute("UPDATE_MENU_PICTURE", username, password, filePath);


                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
        }





    }
    private Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int maxWidth = 1920;
        int maxHeight = 1080;
        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }



    public void save(View view) {

        String offers = profile_special_offers.getText().toString();
        String mail = profile_mail.getText().toString();
        String description = profile_description.getText().toString();
        String number = profile_phone_number.getText().toString();
        String website = profile_website.getText().toString();
        String truck_name = profile_truck_names.getText().toString();
        String slogan = profile_slogan.getText().toString();
        final String username = sharedPreferences.getString("username", null);
        final String password = sharedPreferences.getString("password", null);

        //Other table
        String monday = profile_monday.getText().toString();
        String tuesday = profile_tuesday.getText().toString();
        String wednesday = profile_wednesday.getText().toString();
        String thursday = profile_thursday.getText().toString();
        String friday = profile_friday.getText().toString();
        String saturday = profile_saturday.getText().toString();
        String sunday = profile_sunday.getText().toString();


        if (description.isEmpty() ){
            profile_description.setError("Fill this field to save");
            return;
        }
        if( slogan.isEmpty() ){
            profile_slogan.setError("Fill this field to save");
            return;
        }
        if(truck_name.isEmpty()) {
            profile_truck_names.setError("Fill this field to save");
            return;
        }

        new ServerManager(this, "UPDATE PROFILE").execute("UPDATE PROFILE", mail,username, password, is_working.isChecked() ? "1" : "0",
                description, website, number, offers, slogan, truck_name,monday,tuesday,wednesday,thursday,friday, saturday, sunday, String.valueOf(whichImage));

    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public void startBackgroundfetching(){
        new fetchBackgroundPhoto(username).execute();

    }
    public void startMenufetching(){
        new fetchMenuPhoto(username).execute();
    }


    public void updateLocation(View view) {



        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(enabled) {
            if(!isNetworkAvailable()){
                is_working.setChecked(false);
                new AlertDialog.Builder(AlreadyLoggedIn.this)
                        .setMessage("You need internet connection to do that")
                        .setPositiveButton("UNDERSTOOD", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {



                                dialog.dismiss();
                            }
                        })
                        .show();

                return;
            }

            final double longtitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude();
            final double latitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude();

            new AlertDialog.Builder(AlreadyLoggedIn.this)
                    .setMessage("If you click 'YES', you agree that your location will be accessible to other users  ")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new ServerManager(AlreadyLoggedIn.this, "SEND CORDINATES").execute("SEND CORDINATES", String.valueOf(longtitude), String.valueOf(latitude),
                                    sharedPreferences.getString("username", null), sharedPreferences.getString("password", null));

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            buildAlertMessageNoGps();
        }


    }

    public void butcher_marker(View view) {
        candy_marker.setBackground(null);
        burger_marker.setBackground(null);
        drink_marker.setBackground(null);
        sandwich_marker.setBackground(null);
        butcher_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
        whichImage = 0;
    }

    public void sandwich_marker(View view) {
        candy_marker.setBackground(null);
        burger_marker.setBackground(null);
        drink_marker.setBackground(null);
        sandwich_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
        butcher_marker.setBackground(null);
        whichImage = 1;
    }

    public void burger_marker(View view) {
        candy_marker.setBackground(null);
        burger_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
        drink_marker.setBackground(null);
        sandwich_marker.setBackground(null);
        butcher_marker.setBackground(null);
        whichImage = 2;
    }

    public void candy_marker(View view) {
        candy_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
        burger_marker.setBackground(null);
        drink_marker.setBackground(null);
        sandwich_marker.setBackground(null);
        butcher_marker.setBackground(null);
        whichImage = 3;
    }

    public void drink_marker(View view) {
        candy_marker.setBackground(null);
        burger_marker.setBackground(null);
        drink_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
        sandwich_marker.setBackground(null);
        butcher_marker.setBackground(null);
        whichImage = 4;

    }


    class FetchUserData extends AsyncTask<Void, Void, Void>{

        private Bitmap profile_pic;
        private Bitmap menu_pic;
        private ProgressDialog progressDialog;
        private String username;
        private JSONObject userInfo;
        private JSONArray jsonArray;
        private JSONObject userSchedule;

        public  FetchUserData(String username){
            this.username = username;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AlreadyLoggedIn.this);
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
                HttpPost httpPost = new HttpPost("http://64.137.182.232/fetchProfileInfo.php");


                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("username", username);


                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());

                jsonArray = new JSONArray(responseBody);

                userInfo = jsonArray.getJSONObject(0);
                userSchedule = jsonArray.getJSONObject(1);

                SharedPreferences sharedPreferences = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);

                String link_background_photo = "http://64.137.182.232/pictures/" + username + "." + userInfo.getString("ext_profile");
                String link_menu_photo= "http://64.137.182.232/pictures/" + username +"_menu." + userInfo.getString("ext_menu");

                Log.i("TEST", link_background_photo);
                Log.i("TEST", link_menu_photo);

                profile_pic = getBitmapFromURL(link_background_photo);
                menu_pic = getBitmapFromURL(link_menu_photo);

                Log.i("TEST", "Username" + username + " background_photo " + link_background_photo + " menu photo" + link_menu_photo);



            } catch (Exception e) {

            }




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            try {
                //GENERAL info
                profile_mail.setText(userInfo.getString("mail"));
                profile_phone_number.setText(userInfo.getString("phone_number"));
                profile_description.setText(userInfo.getString("description"));
                profile_slogan.setText(userInfo.getString("slogan"));
                profile_website.setText(userInfo.getString("website"));
                is_working.setChecked(userInfo.getInt("is_working") == 1 ? true : false);
                profile_truck_names.setText(userInfo.getString("truck_name"));
                profile_special_offers.setText(userInfo.getString("special_offer"));

                profile_background_photo.setImageBitmap(profile_pic);
                profile_menu_photo.setImageBitmap(menu_pic);


                whichImage = Integer.parseInt(userInfo.getString("marker_icon"));


                switch (whichImage){
                    case 0:
                        candy_marker.setBackground(null);
                        burger_marker.setBackground(null);
                        drink_marker.setBackground(null);
                        sandwich_marker.setBackground(null);
                        butcher_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
                        break;
                    case 1:
                        candy_marker.setBackground(null);
                        burger_marker.setBackground(null);
                        drink_marker.setBackground(null);
                        sandwich_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
                        butcher_marker.setBackground(null);
                        break;
                    case 2:
                        candy_marker.setBackground(null);
                        burger_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
                        drink_marker.setBackground(null);
                        sandwich_marker.setBackground(null);
                        butcher_marker.setBackground(null);
                        break;
                    case 3:
                        candy_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
                        burger_marker.setBackground(null);
                        drink_marker.setBackground(null);
                        sandwich_marker.setBackground(null);
                        butcher_marker.setBackground(null);
                        break;
                    case 4:
                        candy_marker.setBackground(null);
                        burger_marker.setBackground(null);
                        drink_marker.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_borders_green));
                        sandwich_marker.setBackground(null);
                        butcher_marker.setBackground(null);
                        break;
                }

                //SCHEDULE info
                profile_monday.setText(userSchedule.getString("monday"));
                profile_tuesday.setText(userSchedule.getString("tuesday"));
                profile_wednesday.setText(userSchedule.getString("wednesday"));
                profile_thursday.setText(userSchedule.getString("thursday"));
                profile_friday.setText(userSchedule.getString("friday"));
                profile_saturday.setText(userSchedule.getString("saturday"));
                profile_sunday.setText(userSchedule.getString("sunday"));



            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(aVoid);
        }
    }

    class fetchBackgroundPhoto extends AsyncTask<Void, Void, Void> {

        private String username;
        private Bitmap profile_pic;
        private ProgressDialog progressDialog;

        public fetchBackgroundPhoto(String username) {
            this.username = username;
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
                return scaleBitmap(myBitmap);
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
                HttpPost httpPost = new HttpPost("http://64.137.182.232/ext_profile_background.php");


                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("username", username);


                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());

                jsonArrayExtBackground = new JSONArray(responseBody);

                jsonObjectExtBackground = jsonArrayExtBackground.getJSONObject(0);


                SharedPreferences sharedPreferences = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);

                String link_background_photo = "http://64.137.182.232/pictures/" + username + "." + jsonObjectExtBackground.getString("ext_profile");
                Log.i("TEST", link_background_photo);


                profile_pic = getBitmapFromURL(link_background_photo);



            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            profile_background_photo.setImageBitmap(profile_pic);

            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AlreadyLoggedIn.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
    }







    class fetchMenuPhoto extends AsyncTask<Void, Void, Void> {

        private String username;
        private Bitmap profile_pic;
        private ProgressDialog progressDialog;

        public fetchMenuPhoto(String username) {
            this.username = username;
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
                return scaleBitmap(myBitmap);
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
                HttpPost httpPost = new HttpPost("http://64.137.182.232/ext_profile_menu.php");


                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("username", username);


                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());

                jsonArrayExtMenu = new JSONArray(responseBody);

                jsonObjectExtMenu = jsonArrayExtMenu.getJSONObject(0);


                SharedPreferences sharedPreferences = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);

                String link_menu_photo= "http://64.137.182.232/pictures/" + username +"_menu." + jsonObjectExtMenu.getString("ext_menu");
                Log.i("TEST", link_menu_photo);


                profile_pic = getBitmapFromURL(link_menu_photo);



            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            profile_menu_photo.setImageBitmap(profile_pic);

            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AlreadyLoggedIn.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
    }




}