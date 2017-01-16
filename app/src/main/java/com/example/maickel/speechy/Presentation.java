package com.example.maickel.speechy;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
    private static final int WRITE_EXTERNAL_STORAGE = 0;
    private Button savePresentation;
    private boolean permissionGranted;
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
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
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

        showTextCard = (CardView) findViewById(R.id.openTextScreenButton);
        savePresentationCard = (CardView) findViewById(R.id.savePresentationButton);
        savePresentationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    savePresentation();
                }catch (IOException e){
                    e.printStackTrace();
                }
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
                            if(result.size() > 0) {
                                showResults(seconds, result);
                                showTextView();
                            } else {
                                showNoAudioFoundDialog();
                            }
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

    public void showNoAudioFoundDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Geen spraak waargenomen")
                .setMessage("Er is geen spraak waargenomen. Probeer opnieuw.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences prefs = getSharedPreferences("my_prefs", 0);
                        selectedMode = prefs.getString("presentationMode", "");
                        if(selectedMode.equals("presentationMode")){
                            SharedPreferences presentationPrefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor edit = presentationPrefs.edit();
                            edit.putString("presentationMode", "presentationMode");
                            edit.apply();
                            recreate();
                        } else if(selectedMode.equals("practiceMode")){
                            SharedPreferences practicePrefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor edit = practicePrefs.edit();
                            edit.putString("presentationMode", "practiceMode");
                            edit.apply();
                            recreate();
                        }
                    }}).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        permissionGranted = true;
                } else {
                    permissionGranted = false;
                }
                return;
            }
        }
    }

    public void savePresentation() throws IOException {
        permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE);
            }
        }
        if(permissionGranted){
            try {
                File makeDir = new File(getCacheDir(), "SavedPresentations");

                if (!makeDir.exists()) {
                    makeDir.mkdir();
                }
                java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd;HH.mm.ss");
                Date date = new Date();
                String formattedDate = dateFormat.format(date);
                String title = ("Presentatie-" + formattedDate);
                File file = new File(getCacheDir() + "/SavedPresentations", title);
                ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                result.add(title);
                outputStream.writeObject(result);
                outputStream.flush();
                outputStream.close();

                Toast.makeText(this, "Presentatie Opgeslagen", Toast.LENGTH_SHORT).show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }


}
