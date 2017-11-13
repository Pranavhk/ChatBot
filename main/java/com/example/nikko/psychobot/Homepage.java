package com.example.nikko.psychobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void moveToMain(View view){

        Intent i = new Intent(this,ChatActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
