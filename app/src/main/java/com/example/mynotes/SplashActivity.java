package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {//

    @Override
    protected void onCreate(Bundle savedInstanceState) {//animation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);//set
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();//user
                if(currentUser==null){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));//null về login
                }else{
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));//vào main
                }
                finish();
            }
        },1000);

    }
}