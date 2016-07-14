package com.example.benas.findfood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class ActivateAccount extends AppCompatActivity {


    private ImageView activateIMG;
    private EditText activation_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_rigth_out);
        setContentView(R.layout.activity_activate_account);

//        activateIMG = (ImageView) findViewById(R.id.activate_image);
//        activation_field = (EditText) findViewById(R.id.activation_field);
//
//
//
//
//
//
//        activateIMG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences sharedPreferences = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
//                String username = sharedPreferences.getString("username", null);
//                String password = sharedPreferences.getString("password", null);
//
//                String activation_code = activation_field.getText().toString();
//                new ServerManager(ActivateAccount.this, "ACTIVATE").execute("ACTIVATE",activation_code, username, password);
//            }
//        });


    }

}
