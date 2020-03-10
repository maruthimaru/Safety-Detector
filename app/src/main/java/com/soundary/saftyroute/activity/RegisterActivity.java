package com.soundary.saftyroute.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soundary.saftyroute.R;
import com.soundary.saftyroute.db.AppDatabase;
import com.soundary.saftyroute.db.dao.UserDao;
import com.soundary.saftyroute.db.table.User;
import com.soundary.saftyroute.helper.CommonMethods;

public class RegisterActivity extends AppCompatActivity {


    EditText userName,contactNumber,emailAddress,password,conformPassword;
    TextView goBack,next;
    CommonMethods commonMethods;
    AppDatabase appDatabase;
    UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName=findViewById(R.id.userName);
        contactNumber=findViewById(R.id.contactNumber);
        emailAddress=findViewById(R.id.emailAddress);
        password=findViewById(R.id.password);
        conformPassword=findViewById(R.id.conformPassword);
        goBack=findViewById(R.id.goBack);
        next=findViewById(R.id.next);

        commonMethods=new CommonMethods(this);

        appDatabase=AppDatabase.getInstance(this);
        userDao=appDatabase.userDao();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=userName.getText().toString();
                String contactnumber=contactNumber.getText().toString();
                String emailaddress=emailAddress.getText().toString();
                String pass=password.getText().toString();
                String conformpassword=conformPassword.getText().toString();

                if (username.length()>0){
                    userName.setError(null);
                    if (contactnumber.length()>0){
                        contactNumber.setError(null);
                        if (contactnumber.length()>=10){
                            contactNumber.setError(null);
                        if (emailaddress.length()>0){
                            emailAddress.setError(null);
                            if (commonMethods.EmailValid(emailaddress)){
                                emailAddress.setError(null);
                            if (pass.length()>0){
                                password.setError(null);
                                if (conformpassword.length()>0){
                                    conformPassword.setError(null);
                                    if (conformpassword.equals(pass)){
                                        conformPassword.setError(null);

                                        User user=new User(username,contactnumber,emailaddress,pass);
                                        userDao.userInser(user);
                                        Toast.makeText(getApplicationContext(),"Register successfully ", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                        finish();

                                    }else {
                                        conformPassword.requestFocus();
                                        conformPassword.setError("password's are'n match");
                                    }
                                }else {
                                    conformPassword.requestFocus();
                                    conformPassword.setError("enter conform password");
                                }
                            }else {
                                password.requestFocus();
                                password.setError("enter password");
                            }
                        }else {
                            emailAddress.requestFocus();
                            emailAddress.setError("valid email");
                        }
                        }else {
                            emailAddress.requestFocus();
                            emailAddress.setError("enter emailAddress");
                        }
                    }else {
                        contactNumber.requestFocus();
                        contactNumber.setError("minimum 10 number");
                    }
                    }else {
                        contactNumber.requestFocus();
                        contactNumber.setError("enter contactNumber");
                    }
                }else {
                    userName.requestFocus();
                    userName.setError("enter name");
                }

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }
}
