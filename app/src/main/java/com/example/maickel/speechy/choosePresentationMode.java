package com.example.maickel.speechy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class choosePresentationMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_presentation_mode);
    }

    public void openPresentationMode(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("presentationMode", "presentationMode");
        startActivity(intent);
    }

    public void openPracticeMode(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("presentationMode", "practiceMode");
        startActivity(intent);
    }
}
