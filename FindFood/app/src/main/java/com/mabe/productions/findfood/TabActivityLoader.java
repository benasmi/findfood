package com.mabe.productions.findfood;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.mabe.productions.findfood.R;


public class TabActivityLoader extends AppCompatActivity {
    public static TabLayout tabLayout;
    public static RelativeLayout tab_relative_layout;
    private ViewPager viewPager;
    private EditText input;
    public static boolean isChecked;
    public Menu menuItem;

    private SharedPreferences sharedPreferences;
    public static String[] tabs = {"Profile", "Map"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_activity_loader);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
        setSupportActionBar(myToolbar);

        tabLayout = (TabLayout) findViewById(R.id.my_tab_layout);
        tab_relative_layout = (RelativeLayout) findViewById(R.id.tab_layout);

        sharedPreferences = getSharedPreferences("DataPrefs",MODE_PRIVATE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        viewPager.getCurrentItem();





        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {



                if(tab.getPosition() == 0) {
                    AlreadyLoggedIn.shouldScroll = false;
                }else{
                    AlreadyLoggedIn.shouldScroll = true;
                }

                if(tab.getPosition() == 0) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TabActivityLoader.tab_relative_layout.getLayoutParams();
                    params.topMargin = (int) CheckingUtils.convertPixelsToDp(50,TabActivityLoader.this);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem.getItem(0).setChecked(isChecked);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar,menu);
        menuItem = menu;



        menuItem.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                String username = sharedPreferences.getString("username","");
                String password = sharedPreferences.getString("password","");

                if (!CheckingUtils.isNetworkConnected(TabActivityLoader.this)) {
                    CheckingUtils.createErrorBox("You need internet connection to do that",TabActivityLoader.this);
                    return false;
                } else {
                    menuItem.setChecked(!menuItem.isChecked());
                    isChecked = menuItem.isChecked();
                    CheckingUtils.createErrorBox("Don't forget to change status, after you finish your work", TabActivityLoader.this);
                    new ServerManager(TabActivityLoader.this).execute("CHANGE_IS_WORKING", menuItem.isChecked() ? "1" : "0", username, password);
                }
                return false;
            }
        });

        menuItem.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                CheckingUtils.buildAlertMessageLogout("Do you want to log out?", TabActivityLoader.this);
                return false;
            }
        });

        //Update Location
        menuItem.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                update_location();
                return false;
            }
        });

       menuItem.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem menuItem) {

               AlertDialog.Builder builder;
               final EditText input = new EditText(TabActivityLoader.this);
                input.getBackground().setColorFilter(Color.parseColor("#3498db"), PorterDuff.Mode.SRC_ATOP);
               builder = new AlertDialog.Builder(TabActivityLoader.this);
               builder.setTitle("Location by adress");
               builder.setMessage("Enter adress of your truck");
               builder.setView(input);
               builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       if(CheckingUtils.isNetworkConnected(TabActivityLoader.this)){
                           try {
                               updateManually(input.getText().toString());
                           } catch (Exception e) {

                               CheckingUtils.createErrorBox("Invalid address", TabActivityLoader.this);

                               e.printStackTrace();


                           }

                       }else{
                           return;
                       }



                   }
               });
               builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();


                   }
               });



               builder.show();

                   return false;
           }
       });

        return super.onCreateOptionsMenu(menu);
    }


    public void update_location(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (enabled) {
            if (!CheckingUtils.isNetworkConnected(this)) {
                CheckingUtils.createErrorBox("You need internet connection to do that", this);
                return;
            }

            final double longtitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude();
            final double latitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude();

            CheckingUtils.createErrorBox("Your location was updated", this);
            new ServerManager(this).execute("SEND CORDINATES", String.valueOf(longtitude), String.valueOf(latitude),
                    sharedPreferences.getString("username", null), sharedPreferences.getString("password", null));
        } else {
            CheckingUtils.buildAlertMessageNoGps("Your GPS seems disabled, do you want to enable it?", this);
        }
    }

    public void updateManually(String adress) throws Exception{

        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyD7CmlEdYr_-nU6pNDxik_8FTq-tD53iw8");
        GeocodingResult[] results =  GeocodingApi.geocode(context, adress).await();

        double latitude = results[0].geometry.location.lat;
        double longtitude = results[0].geometry.location.lng;

        new ServerManager(this).execute("SEND CORDINATES", String.valueOf(longtitude), String.valueOf(latitude),
                sharedPreferences.getString("username", null), sharedPreferences.getString("password", null));

    }






}
