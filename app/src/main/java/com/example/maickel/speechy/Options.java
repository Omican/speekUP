package com.example.maickel.speechy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    public void addKeywordScreen(View v){
        Intent intent = new Intent(getApplicationContext(), addKeyWords.class);
        startActivity(intent);
    }
}
