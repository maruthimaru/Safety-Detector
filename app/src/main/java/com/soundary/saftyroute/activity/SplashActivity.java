package com.soundary.saftyroute.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.soundary.saftyroute.R;
import com.soundary.saftyroute.db.AppDatabase;

public class SplashActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appDatabase= AppDatabase.getInstance(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent( SplashActivity.this,MainActivity.class));
                finish();
            }
        },2000);

    }
}
