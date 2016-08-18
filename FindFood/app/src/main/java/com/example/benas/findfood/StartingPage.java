package com.example.benas.findfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ;
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (enabled) {
            startActivity(new Intent(this, TrucksMapBeggining.class));
        } else {
            CheckingUtils.buildAlertMessageNoGps("You need GPS to do it, do you want to enable it?", StartingPage.this);
        }
    }

}
