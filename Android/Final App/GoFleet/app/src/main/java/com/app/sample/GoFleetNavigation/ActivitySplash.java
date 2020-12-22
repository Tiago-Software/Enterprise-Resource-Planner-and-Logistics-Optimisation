package com.app.sample.GoFleetNavigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActivitySplash extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        };
        new Timer().schedule(task,2000);



    }
}
