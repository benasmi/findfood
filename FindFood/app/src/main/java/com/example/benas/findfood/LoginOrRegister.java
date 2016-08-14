package com.example.benas.findfood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginOrRegister extends AppCompatActivity {


    private EditText username_login;
    private EditText password_login;
    private TextView loginText;
    private CheckBox remember_my_password;
    private boolean rememberPassword = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_rigth_out);
        setContentView(R.layout.activity_login_or_register);

        username_login = (EditText) findViewById(R.id.username_login);
        password_login = (EditText) findViewById(R.id.password_login);
        remember_my_password = (CheckBox) findViewById(R.id.remember_my_password);

        username_login.setText("");
        password_login.setText("");

        loginText = (TextView) findViewById(R.id.useless);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/manteka.ttf");

        loginText.setTypeface(tf);

        remember_my_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberPassword = isChecked;

            }
        });
        loginFromMemory();


    }

    //Login by using sharedPrefs credentials
    public void loginFromMemory() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
        remember_my_password.setChecked(sharedPreferences.getBoolean("rememberPassword", false));
        if (sharedPreferences.getBoolean("rememberPassword", false) == true) {
            username_login.setText(sharedPreferences.getString("username", ""));
            password_login.setText(sharedPreferences.getString("password", ""));
        }
    }


    //Go to register activity
    public void register(View view) {
        startActivity(new Intent(this, Registration.class));
    }


    //Checking username and password with database.
    public void login(View view) {
        String username = username_login.getText().toString();
        String password = password_login.getText().toString();

        if (username.isEmpty()) {
            username_login.setError("Enter username!");
            return;
        }
        if (password.isEmpty()) {
            password_login.setError("Enter password!");
            return;
        }
        new ServerManager(this).execute("LOGIN", username.toLowerCase(), password.toLowerCase(), rememberPassword ? "1" : "0");
    }


    public void forgotInfo(View view) {
        startActivity(new Intent(this, ForgotPasswordOrUsername.class));
    }
}
