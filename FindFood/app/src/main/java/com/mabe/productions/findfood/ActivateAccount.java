package com.mabe.productions.findfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.mabe.productions.findfood.R;

public class ActivateAccount extends AppCompatActivity {


    private ImageView activateIMG;
    private EditText activation_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_rigth_out);
        setContentView(R.layout.activity_activate_account);

    }

}
