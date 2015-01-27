package com.example.yan_home.openglengineandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.yan_home.openglengineandroid.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {

                System.gc();

                //TODO : load assets here
                Intent myIntent = new Intent(SplashActivity.this, MainMenuActivity.class);
                //Optional parameters
//                myIntent.putExtra("key", value);
                SplashActivity.this.startActivity(myIntent);

                //TODO : custom transition animation ?
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                //we no longer need this activity
                finish();
            }
        }, 2000);

    }

}
