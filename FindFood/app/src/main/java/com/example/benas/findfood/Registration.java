package com.example.benas.findfood;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Registration extends AppCompatActivity {


    private EditText password;
    private EditText repeat_password;
    private EditText mail;
    private EditText username;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.push_right_in, R.anim.push_rigth_out);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_registration);


        password = (EditText) findViewById(R.id.password_login);
        repeat_password = (EditText) findViewById(R.id.repeat_password);
        mail = (EditText) findViewById(R.id.mail);
        username = (EditText) findViewById(R.id.username_login);
        register = (TextView) findViewById(R.id.registerTextView);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/manteka.ttf");

        register.setTypeface(tf);

    }

    public void register(View view) {

        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String passrepeat = repeat_password.getText().toString().trim();
        String email = mail.getText().toString().trim();


        if (user.isEmpty()) {
            username.setError("This field cannot be empty");
            return;
        }
        if (user.contains(" ")) {
            username.setError("This field cannot contain spaces");
            return;
        }
        if (user.length() < 6 || user.length() > 32) {
            username.setError("Username must contain 6-32 symbols");
            return;
        }

        if (pass.isEmpty()) {
            password.setError("This field cannot be empty");
            return;
        }
        if (pass.contains(" ")) {
            password.setError("This field cannot contain spaces");
            return;
        }
        if (pass.length() < 6 || pass.length() > 32) {
            password.setError("Password must contain 6-32 symbols");
            return;
        }
        if (passrepeat.isEmpty()) {
            repeat_password.setError("This field cannot be empty");
            return;
        }
        if (!passrepeat.equals(pass)) {
            password.setText("");
            repeat_password.setText("");
            password.setError("Password doesn't match");
            repeat_password.setError("Password doesn't match");
            return;
        }
        if (email.isEmpty() || !email.contains("@")) {
            mail.setError("Enter valid email");
            return;
        }
        if (email.contains(" ")) {
            mail.setError("This field cannot contain space");
        }


        new ServerManager(this).execute("REGISTRATION",
                user.toLowerCase(),
                pass.toLowerCase(),
                email);
    }

}

