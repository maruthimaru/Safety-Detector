package com.soundary.saftyroute.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soundary.saftyroute.R;
import com.soundary.saftyroute.db.AppDatabase;
import com.soundary.saftyroute.db.dao.UserDao;
import com.soundary.saftyroute.db.table.User;

public class LoginActivity extends AppCompatActivity {


    EditText mobile,pins;
    TextView textViewBottom,logein;
    AppDatabase appDatabase;
    UserDao userDao;
    private String TAG=LoginActivity.class.getSimpleName();
    private boolean doubleBackpress=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobile=findViewById(R.id.mobile);
        pins=findViewById(R.id.pins);
        textViewBottom=findViewById(R.id.textViewBottom);
        logein=findViewById(R.id.logein);

        appDatabase=AppDatabase.getInstance(this);
        userDao=appDatabase.userDao();

        logein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNum=mobile.getText().toString();
                String pass=pins.getText().toString();
                if (mobileNum.length()>0){
                    mobile.setError(null);
                    if (mobileNum.length()>=10) {
                        mobile.setError(null);
                        if (pass.length() > 0) {
                            pins.setError(null);
                            User user=userDao.getValidUser(mobileNum,pass);
                            Log.e(TAG, "onClick: "+user );
                            if (user==null){
                                Toast.makeText(getApplicationContext(),"Invalid user" , Toast.LENGTH_SHORT).show();
                            }else {
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }

                        } else {
                            pins.requestFocus();
                            pins.setError("enter password");
                        }
                    }else {
                        mobile.requestFocus();
                        mobile.setError("minimum 10");
                    }
                }else {
                    mobile.requestFocus();
                    mobile.setError("enter mobile number");
                }

            }
        });

        textViewBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
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
