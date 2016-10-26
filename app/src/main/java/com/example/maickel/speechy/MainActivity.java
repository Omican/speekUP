package com.example.maickel.speechy;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private long startTime;
    private ArrayList<String> result;
    private Set<String> keyWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        SharedPreferences prefs = getSharedPreferences("my_prefs", 0);
        keyWords = prefs.getStringSet("KeyWords", new HashSet<String>());

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startTime = System.currentTimeMillis();
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    double seconds = elapsedTime / 1000.0;

                    result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String[] list = result.get(0).split(" ");
                    ArrayList<String> keyWordsList = new ArrayList<String>(keyWords);

                    txtSpeechInput.setText("Je hebt " + countKeyWords(keyWordsList, keyWords.size(), result).keySet() + "\n " + countKeyWords(keyWordsList, keyWords.size(),result).values());

                    //txtSpeechInput.setText("Je hebt " + countWords(result) + " woorden gesproken" + " \n In " + seconds + " seconden"
                    //+ "\n\n Het meest voorkomende woord is " + countMostRepeated(list).getKey() + " met " + countMostRepeated(list).getValue() + " keer");
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void openTextView(View v){
        //Retrieve keywords from addkeyword screen
        //List<String> keyWordsList = new ArrayList<String>(keyWords);
        //Log.v("Value:", keyWordsList.get(0));
        Intent intent = new Intent(getApplicationContext(), ShowText.class);
        intent.putExtra("SpokenText", result);
        startActivity(intent);
    }

    public Map<String, Integer> countKeyWords(ArrayList<String> keyWords, Integer wordAmount, ArrayList<String> result){
        Map<String, Integer> keyWordCount = new HashMap<>();

        for(int y = 0; y < keyWords.size(); y++){
            int inputAmount = 0;
            for(int x = 0; x < wordAmount; x++) {
                String[] words = result.get(0).split(" ");
                if (words[x].equals(keyWords.get(y))) {
                    inputAmount++;
                }
                keyWordCount.put(keyWords.get(y), inputAmount);
            }
        }
        return keyWordCount;
    }

    public Map.Entry<String, Integer> countMostRepeated(String[] list){
        Map<String, Integer> stringsCount = new HashMap<>();

        for(String s : list){
            if(s != "de" && s!= "het" && s != "een" && s != "De" && s != "Het" && s != "Een" ) {
                Integer c = stringsCount.get(s);
                if (c == null) c = new Integer(0);
                c++;
                stringsCount.put(s, c);
            }
        }

        Map.Entry<String, Integer> mostRepeated = null;

        for(Map.Entry<String, Integer> e: stringsCount.entrySet()){
            if(mostRepeated == null || mostRepeated.getValue() < e.getValue()){
                mostRepeated = e;
            }
        }

        return mostRepeated;
    }

    public Integer countWords (ArrayList<String> result){
        Integer wordAmount = result.get(0).trim().split(" ").length;
        return wordAmount;
    }


}
