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

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionService;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends Activity{

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private SpeechRecognizer mSpeechRecognizer = null;
    private Intent mSpeechRecognizerIntent;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private long startTime;
    private ArrayList<String> result = new ArrayList<>();
    private ArrayList<String> tempResult;
    private Set<String> keyWords;
    private TextView spokenWords;
    private TextView timeElapsed;
    private TextView mostRepeated;
    private TextView spokenWordsTitle;
    private TextView timeElapsedTitle;
    private TextView mostRepeatedTitle;
    private Boolean presentationAlerts;
    private Boolean practiceAlerts;
    private int presentationTime;
    private int practiceTime;
    private String[] list;
    private double seconds;
    private ToggleButton startSpeech;
    private ProgressBar speechProgress;
    private String LOG_TAG = "VoiceRecognition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.i(LOG_TAG, "onBeginningOfSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.i(LOG_TAG, "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                Log.i(LOG_TAG, "onEndOfSpeech");

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                Log.i(LOG_TAG, "onResults");
                long elapsedTime = System.currentTimeMillis() - startTime;
                seconds = elapsedTime / 1000.0;

                tempResult = results
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                list = tempResult.get(0).split(" ");

                for (int i = 0; i < list.length; i++) {
                    result.add(list[i]);
                }
                ArrayList<String> keyWordsList = new ArrayList<String>(keyWords);

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "nl");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        //btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        spokenWords = (TextView) findViewById(R.id.spokenWords);
        timeElapsed = (TextView) findViewById(R.id.elapsedTime);
        mostRepeated = (TextView) findViewById(R.id.mostRepeated);
        spokenWordsTitle = (TextView) findViewById(R.id.spokenWordsTitle);
        timeElapsedTitle = (TextView) findViewById(R.id.timeElapsedTitle);
        mostRepeatedTitle = (TextView) findViewById(R.id.mostRepeatedTitle);
        startSpeech = (ToggleButton) findViewById(R.id.toggleButton);
        speechProgress = (ProgressBar) findViewById(R.id.progressBar);

        SharedPreferences prefs = getSharedPreferences("my_prefs", 0);
        keyWords = prefs.getStringSet("KeyWords", new HashSet<String>());
        presentationAlerts = prefs.getBoolean("presentationAlerts", false);
        practiceAlerts = prefs.getBoolean("practiceAlerts", false);
        presentationTime = prefs.getInt("presentationTime", 0);
        practiceTime = prefs.getInt("practiceTime", 0);

        speechProgress.setVisibility(View.INVISIBLE);
        hideTextViews();

        startSpeech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    hideTextViews();
                    result.clear();
                    startTime = System.currentTimeMillis();
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    speechProgress.setVisibility(View.VISIBLE);
                    speechProgress.setIndeterminate(true);
                } else {
                    if (result.size() != 0) {
                        spokenWords.setText(" " + countWords(result));
                        timeElapsed.setText(" " + seconds + " seconden");
                        mostRepeated.setText("" + countMostRepeated(result).getKey() + "(" + countMostRepeated(result).getValue() + ")");
                    }
                    speechProgress.setIndeterminate(false);
                    speechProgress.setVisibility(View.INVISIBLE);
                    mSpeechRecognizer.stopListening();
                    showTextView();
                }
            }
        });
    }


    public void openTextView(View v){
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

    public Map.Entry<String, Integer> countMostRepeated(ArrayList<String> list){
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
        ArrayList<String> tempArray = new ArrayList<>();
        for(int i = 0; i < result.size(); i++){
            String[] temp = result.get(i).split(" ");
            for(int j = 0; j < temp.length; j++){
                tempArray.add(temp[j]);
            }
        }
        Integer wordAmount = tempArray.size();
        return wordAmount;
    }

    public void hideTextViews(){
        spokenWords.setVisibility(View.GONE);
        spokenWordsTitle.setVisibility(View.GONE);
        timeElapsed.setVisibility(View.GONE);
        timeElapsedTitle.setVisibility(View.GONE);
        mostRepeated.setVisibility(View.GONE);
        mostRepeatedTitle.setVisibility(View.GONE);
    }

    public void showTextView(){
        spokenWords.setVisibility(View.VISIBLE);
        spokenWordsTitle.setVisibility(View.VISIBLE);
        timeElapsed.setVisibility(View.VISIBLE);
        timeElapsedTitle.setVisibility(View.VISIBLE);
        mostRepeated.setVisibility(View.VISIBLE);
        mostRepeatedTitle.setVisibility(View.VISIBLE);
    }


}
