package com.mabe.productions.findfood;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mabe.productions.findfood.R;

public class ForgotPasswordOrUsername extends AppCompatActivity {

    private EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_or_username);

        TextView text = (TextView) findViewById(R.id.password_recovery_text);
        TextView recoverytext = (TextView) findViewById(R.id.recovery_text);
        mail = (EditText) findViewById(R.id.profile_mail);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/manteka.ttf");

        text.setTypeface(tf);
        recoverytext.setTypeface(tf);


    }

    public void recoverPass(View view) {
        String email = mail.getText().toString();
        if (mail.equals("")) {
            return;
        }

        new ServerManager(this).execute("RECOVERY", email);


    }
}
