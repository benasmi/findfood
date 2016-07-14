package com.example.benas.findfood;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TabHost;
import android.widget.TextView;

public class TabActivityLoader extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_rigth_out);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_activity_loader);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);



        TabHost.TabSpec tabSpec = tabHost.newTabSpec("profile");
        tabSpec.setContent(new Intent(this, AlreadyLoggedIn.class));
        tabSpec.setIndicator("Profile");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("map");
        tabSpec.setContent(new Intent(this, TrucksMap.class));
        tabSpec.setIndicator("Map");
        tabHost.addTab(tabSpec);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

    }


}
