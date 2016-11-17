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
import android.os.Handler;
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

    private SpeechRecognizer mSpeechRecognizer = null;
    private Intent mSpeechRecognizerIntent;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private Button showTextView;
    private long startTime;
    private ArrayList<String> result = new ArrayList<>();
    private ArrayList<String> tempResult;
    private Set<String> keyWords;
    private TextView spokenWords;
    private TextView micText;
    private TextView timeElapsed;
    private TextView mostRepeated;
    private TextView spokenWordsTitle;
    private TextView timeElapsedTitle;
    private TextView mostRepeatedTitle;
    private Boolean showKeyWords;
    private Boolean showMostSpokenWords;
    private Boolean enableAlerts;
    private int timeLimit;
    private String selectedMode;
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
                Log.i(LOG_TAG, "partialResults");
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
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(100));
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        micText = (TextView) findViewById(R.id.micText);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        spokenWords = (TextView) findViewById(R.id.spokenWords);
        timeElapsed = (TextView) findViewById(R.id.elapsedTime);
        mostRepeated = (TextView) findViewById(R.id.mostRepeated);
        spokenWordsTitle = (TextView) findViewById(R.id.spokenWordsTitle);
        timeElapsedTitle = (TextView) findViewById(R.id.timeElapsedTitle);
        mostRepeatedTitle = (TextView) findViewById(R.id.mostRepeatedTitle);
        startSpeech = (ToggleButton) findViewById(R.id.toggleButton);
        speechProgress = (ProgressBar) findViewById(R.id.progressBar);
        showTextView = (Button) findViewById(R.id.openTextScreen);

        SharedPreferences prefs = getSharedPreferences("my_prefs", 0);
        keyWords = prefs.getStringSet("KeyWords", new HashSet<String>());
        selectedMode = prefs.getString("presentationMode", "");
        if(selectedMode.equals("presentationMode")){
            enableAlerts = prefs.getBoolean("presentationAlerts", false);
            timeLimit = prefs.getInt("presentationTime", 0);
            showKeyWords = prefs.getBoolean("presentationKeywords", false);
            showMostSpokenWords = prefs.getBoolean("presentationMostSpokenWords", false);
        }
        if(selectedMode.equals("practiceMode")){
            enableAlerts = prefs.getBoolean("practiceAlerts", false);
            timeLimit = prefs.getInt("practiceTime", 0);
            showKeyWords = prefs.getBoolean("practiceKeywords", false);
            showMostSpokenWords = prefs.getBoolean("practiceMostSpokenWords", false);
        }

        speechProgress.setVisibility(View.INVISIBLE);
        hideTextViews();

        startSpeech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    hideTextViews();
                    micText.setVisibility(View.INVISIBLE);
                    result.clear();
                    startTime = System.currentTimeMillis();
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    speechProgress.setVisibility(View.VISIBLE);
                    speechProgress.setIndeterminate(true);
                } else {
                    micText.setVisibility(View.VISIBLE);
                    speechProgress.setIndeterminate(false);
                    speechProgress.setVisibility(View.INVISIBLE);
                    mSpeechRecognizer.stopListening();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showResults(seconds, result);
                            showTextView();
                        }
                    },1000
                    );

                }
            }
        });
    }


    public void openTextView(View v){
        Intent intent = new Intent(getApplicationContext(), ShowText.class);
        intent.putExtra("SpokenText", result);
        startActivity(intent);
    }

    public StringBuilder countKeyWords(Set<String> keyWords, Integer wordAmount, ArrayList<String> result){
        Map<String, Integer> keyWordCount = new HashMap<>();
        StringBuilder temp = new StringBuilder();
        List<String> list = new ArrayList<>(keyWords);

        for(int y = 0; y < keyWords.size(); y++){
            int inputAmount = 0;
            for(int x = 0; x < wordAmount; x++) {
               //String[] words = result.get(0).split(" ");
                if (list.get(y).equals(result.get(x))) {
                    inputAmount++;
                }
            }
            keyWordCount.put(list.get(y), inputAmount);
        }

        for(Map.Entry<String, Integer> entry : keyWordCount.entrySet()){
            temp.append("-"+entry.getKey() + "(" + entry.getValue() + ")" + System.lineSeparator());
        }
        return temp;
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
        spokenWords.setVisibility(View.INVISIBLE);
        spokenWordsTitle.setVisibility(View.INVISIBLE);
        timeElapsed.setVisibility(View.INVISIBLE);
        timeElapsedTitle.setVisibility(View.INVISIBLE);
        mostRepeated.setVisibility(View.INVISIBLE);
        mostRepeatedTitle.setVisibility(View.INVISIBLE);
        showTextView.setVisibility(View.INVISIBLE);
    }

    public void showTextView(){
        spokenWords.setVisibility(View.VISIBLE);
        spokenWordsTitle.setVisibility(View.VISIBLE);
        timeElapsed.setVisibility(View.VISIBLE);
        timeElapsedTitle.setVisibility(View.VISIBLE);
        mostRepeated.setVisibility(View.VISIBLE);
        mostRepeatedTitle.setVisibility(View.VISIBLE);
        showTextView.setVisibility(View.VISIBLE);
    }

    public void showResults(double seconds, ArrayList<String> result){
        if (result.size() > 0) {
            spokenWords.setText(String.format(Integer.toString(countWords(result))));
            timeElapsed.setText(String.format(Double.toString(seconds) + " seconden"));
            if(showMostSpokenWords == true) {
                mostRepeated.setText(String.format(countKeyWords(keyWords, countWords(result), result).toString()));
            }
            if(showKeyWords == true) {
                mostRepeated.setText(String.format(countMostRepeated(result).getKey() + "(" + Integer.toString(countMostRepeated(result).getValue()) + ")"));
            }
        }
    }


}
