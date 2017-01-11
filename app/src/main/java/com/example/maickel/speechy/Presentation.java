package com.example.maickel.speechy;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Presentation extends Activity{

    //region
    private SpeechRecognizer mSpeechRecognizer = null;
    private Intent mSpeechRecognizerIntent;
    private TextView txtSpeechInput;
    private PresentationLogic presentationLogic;
    private ImageButton btnSpeak;
    private CardView showTextCard;
    private CardView savePresentationCard;
    private Button showTextView;
    private long startTime;
    private ArrayList<String> result = new ArrayList<>();
    private ArrayList<String> tempResult;
    private Set<String> keyWords;
    private CardView spokenWordsCard;
    private CardView elapsedTimeCard;
    private CardView mostSpokenCard;
    private CardView countdownCard;
    private TextView spokenWords;
    private TextView micText;
    private TextView timeElapsed;
    private TextView mostRepeated;
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
    private long timeLimitInMills;
    private TextView countDownTimer;
    private CountDownTimer timer;
    private Button savePresentation;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presentationLogic = new PresentationLogic();

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

                ArrayList<String>  partial = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                ArrayList<String> temp = new ArrayList<String>(keyWords);
                    if(enableAlerts) {
                    for (int i = 0; i < temp.size(); i++) {
                        if (partial.get(0).equals(temp.get(i))) {
                            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(500);
                        }
                    }
                }
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

        spokenWordsCard = (CardView) findViewById(R.id.spokenWordsCard);
        elapsedTimeCard = (CardView) findViewById(R.id.elapsedTimeCard);
        mostSpokenCard = (CardView) findViewById(R.id.mostSpokenCard);
        countdownCard = (CardView) findViewById(R.id.countdownCard);
        savePresentationCard = (CardView) findViewById(R.id.savePresentationButton);
        showTextCard = (CardView) findViewById(R.id.openTextScreenButton);

        savePresentationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        showTextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTextView(v);
            }
        });

        spokenWords = (TextView) findViewById(R.id.spokenWords);
        timeElapsed = (TextView) findViewById(R.id.elapsedTime);
        mostRepeated = (TextView) findViewById(R.id.mostRepeated);
        mostRepeatedTitle = (TextView) findViewById(R.id.mostRepeatedTitle);

        micText = (TextView) findViewById(R.id.micText);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        startSpeech = (ToggleButton) findViewById(R.id.toggleButton);
        speechProgress = (ProgressBar) findViewById(R.id.progressBar);
        countDownTimer = (TextView) findViewById(R.id.countDownTimer);

        getPreferences();

        timeLimitInMills = timeLimit * 60000;

        speechProgress.setVisibility(View.INVISIBLE);
        hideTextViews();

        startSpeech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         final boolean isChecked) {
                if (isChecked) {
                    hideTextViews();
                    micText.setVisibility(View.INVISIBLE);
                    result.clear();
                    startTime = System.currentTimeMillis();
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    speechProgress.setVisibility(View.VISIBLE);
                    speechProgress.setIndeterminate(true);
                    countdownCard.setVisibility(View.VISIBLE);
                    timer = new CountDownTimer(timeLimitInMills, 1000){
                        public void onTick(long millisUntilFinished){
                            countDownTimer.setText("Resterende Tijd: " + presentationLogic.convertSecondsToMmSs(millisUntilFinished));
                            if(millisUntilFinished > 59800 && millisUntilFinished < 61800){
                                if(enableAlerts){
                                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(500);
                                    v.vibrate(500);
                                }
                            }
                        }
                        public void onFinish(){
                            countdownCard.setVisibility(View.INVISIBLE);
                        }
                    }.start();
                } else {
                    timer.cancel();
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

    public void hideTextViews(){
        spokenWordsCard.setVisibility(View.INVISIBLE);
        mostSpokenCard.setVisibility(View.INVISIBLE);
        elapsedTimeCard.setVisibility(View.INVISIBLE);
        countdownCard.setVisibility(View.INVISIBLE);
        showTextCard.setVisibility(View.INVISIBLE);
        savePresentationCard.setVisibility(View.INVISIBLE);
    }

    public void showTextView(){
        spokenWordsCard.setVisibility(View.VISIBLE);
        mostSpokenCard.setVisibility(View.VISIBLE);
        elapsedTimeCard.setVisibility(View.VISIBLE);
        countdownCard.setVisibility(View.INVISIBLE);
        showTextCard.setVisibility(View.VISIBLE);
        savePresentationCard.setVisibility(View.VISIBLE);
    }

    public void showResults(double seconds, ArrayList<String> result){
        if (result.size() > 0) {
            spokenWords.setText(String.format(Integer.toString(presentationLogic.countWords(result))));
            timeElapsed.setText(String.format(Double.toString(seconds) + " seconden"));
            if(showKeyWords) {
                mostRepeatedTitle.setText(R.string.showKeywords);
                mostRepeated.setText(String.format(presentationLogic.countKeyWords(keyWords, presentationLogic.countWords(result), result).toString()));
            }
            if(showMostSpokenWords) {
                mostRepeatedTitle.setText(R.string.showMostSpoken);
                mostRepeated.setText(String.format(presentationLogic.countMostRepeated(result).getKey() + "(" + Integer.toString(presentationLogic.countMostRepeated(result).getValue()) + ")"));
            }
        }
    }

    public void getPreferences(){
        SharedPreferences prefs = getSharedPreferences("my_prefs", 0);
        keyWords = prefs.getStringSet("KeyWords", new HashSet<String>());
        selectedMode = prefs.getString("presentationMode", "");
        if(selectedMode.equals("presentationMode")){
            enableAlerts = prefs.getBoolean("presentationAlerts", false);
            timeLimit = prefs.getInt("presentationTime", 0);
            showKeyWords = prefs.getBoolean("presentationShowKeywords", false);
            showMostSpokenWords = prefs.getBoolean("presentationMostSpokenWords", false);
        }
        if(selectedMode.equals("practiceMode")){
            enableAlerts = prefs.getBoolean("practiceAlerts", false);
            timeLimit = prefs.getInt("practiceTime", 0);
            showKeyWords = prefs.getBoolean("practiceShowKeywords", false);
            showMostSpokenWords = prefs.getBoolean("practiceMostSpokenWords", false);
        }
    }

    public void savePresentation() throws IOException {
        File file = new File(getDir("data", MODE_PRIVATE), "keyWordsMap");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        Map<String, Integer> keyWordMap = presentationLogic.countKeyWordsMap(keyWords, presentationLogic.countWords(result), result);
        outputStream.writeObject(keyWordMap);
        outputStream.flush();
        outputStream.close();
    }


}
