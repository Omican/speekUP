package com.example.maickel.speechy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class Analytics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        try {
            getKeywords();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getKeywords() throws IOException, ClassNotFoundException {
        File file = new File(getDir("data", 0), "keyWordsMap");
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        Object keywordsMap = inputStream.readObject();
        Log.i("Map",keywordsMap.toString());
    }
}
