package com.example.maickel.speechy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class addKeyWords extends AppCompatActivity {
    public ArrayList<String> keyWords;
    private EditText keyWord0;
    private EditText keyWord1;
    private EditText keyWord2;
    private EditText keyWord3;
    private EditText keyWord4;
    private Set<String> keyWordsSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_key_words);

        keyWord0 = (EditText) findViewById(R.id.keyWord0);
        keyWord1 = (EditText) findViewById(R.id.keyWord1);
        keyWord2 = (EditText) findViewById(R.id.keyWord2);
        keyWord3 = (EditText) findViewById(R.id.keyWord3);
        keyWord4 = (EditText) findViewById(R.id.keyWord4);

        SharedPreferences prefs = getSharedPreferences("my_prefs", 0);
        keyWordsSet = prefs.getStringSet("KeyWords", new HashSet<String>());
        List<String> keyWordsList = new ArrayList<String>(keyWordsSet);

        for(int i = 0; i < keyWordsList.size(); i++){
            if(i == 0){
                keyWord0.setText(keyWordsList.get(i));
            }
            if(i == 1){
                keyWord1.setText(keyWordsList.get(i));
            }
            if(i == 2){
                keyWord2.setText(keyWordsList.get(i));
            }
            if(i == 3){
                keyWord3.setText(keyWordsList.get(i));
            }
            if(i == 4){
                keyWord4.setText(keyWordsList.get(i));
            }
        }
    }

    public void saveKeywords (View v){
        keyWords = new ArrayList<>();
        if(!keyWord0.getText().toString().matches("")) {
            keyWords.add(keyWord0.getText().toString().replaceAll("\\s+",""));
        }
        if(!keyWord1.getText().toString().matches("")) {
            keyWords.add(keyWord1.getText().toString().replaceAll("\\s+",""));
        }
        if(!keyWord2.getText().toString().matches("")) {
            keyWords.add(keyWord2.getText().toString().replaceAll("\\s+",""));
        }
        if(!keyWord3.getText().toString().matches("")) {
            keyWords.add(keyWord3.getText().toString().replaceAll("\\s+",""));
        }
        if(!keyWord4.getText().toString().matches("")) {
            keyWords.add(keyWord4.getText().toString().replaceAll("\\s+",""));
        }
        Set keyWordsSet = new HashSet(keyWords);
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("KeyWords", keyWordsSet);
        edit.apply();
        Intent intent = new Intent(addKeyWords.this, MainScreen.class);
        startActivity(intent);
    }

    public void clearText(View v){
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("KeyWords", null);
        edit.apply();
        finish();
        startActivity(getIntent());
    }
}
