package com.example.maickel.speechy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowText extends AppCompatActivity {

    private TextView showSpokenText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        showSpokenText = (TextView) findViewById(R.id.showSpokenText);

        Bundle extras = getIntent().getExtras();

        ArrayList<String> result = extras.getStringArrayList("SpokenText");

        String temp = "";

        for(int i = 0; i < result.size(); i++){
            temp += result.get(i) + " ";
        }

        showSpokenText.setText(temp);
    }


}
