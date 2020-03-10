package com.soundary.saftyroute.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soundary.saftyroute.R;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackpress=false;
    TextView logein;
    EditText destination,source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logein=findViewById(R.id.logein);
        destination=findViewById(R.id.destination);
        source=findViewById(R.id.source);


        logein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceString=source.getText().toString();
                String destinationString=destination.getText().toString();
                if (sourceString.length()>0){
                    source.setError(null);
                    if (destinationString.length()>0){
                        destination.setError(null);

Intent intent=new Intent(MainActivity.this,MapsActivity.class);
intent.putExtra("source",sourceString);
                        intent.putExtra("destination",destinationString);
                        startActivity(intent);
                        finish();
                    }else {
                        destination.setError("should not be empty");
                        destination.requestFocus();
                    }
                }else {
                    source.setError("should not be empty");
                    source.requestFocus();
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        if (doubleBackpress) {
            super.onBackPressed();
            finishAffinity();
        }
            doubleBackpress=true;
            Toast.makeText(getApplicationContext(),"Backpress Again ",Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackpress=false;
            }
        }, 2000);

    }
}
