package com.mabe.productions.findfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;
import com.mabe.productions.findfood.R;

public class StartingPage extends AppCompatActivity {

    private TextView logo;
    private Button find;
    private Button login;
    private TextView phrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        overridePendingTransition(R.anim.push_right_in, R.anim.push_rigth_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);



        find = (Button) findViewById(R.id.find);
        login = (Button) findViewById(R.id.loginOrRegister);
        logo = (TextView) findViewById(R.id.logo);
        phrase = (TextView) findViewById(R.id.phrase);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/manteka.ttf");
        logo.setTypeface(tf);
        phrase.setTypeface(tf);
        find.setTypeface(tf);
        login.setTypeface(tf);


    }

    public void loginOrRegister(View view) {
        startActivity(new Intent(this, LoginOrRegister.class));
    }






    public void findFood(View view) {


        if (CheckingUtils.isGpsEnabled(this)) {
            if(CheckingUtils.isNetworkConnected(this)){
                startActivity(new Intent(this, TrucksMapBeggining.class));
            }
        } else {
            CheckingUtils.buildAlertMessageNoGps("You need GPS to do it, do you want to enable it?", StartingPage.this);
        }
    }


}
