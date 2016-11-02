package com.example.maickel.speechy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Options extends AppCompatActivity {
    private Switch presentationAlerts;
    private Switch practiceAlerts;
    private EditText presentationTime;
    private EditText practiceTime;
    private Button saveButton;
    private int presentationTimeValue;
    private int practiceTimeValue;
    private Boolean presentationAlertsValue;
    private Boolean practiceAlertsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        presentationAlerts = (Switch) findViewById(R.id.togglePresentationAlerts);
        practiceAlerts = (Switch) findViewById(R.id.toggleTrainingAlerts);
        presentationTime = (EditText) findViewById(R.id.presentationTimeEdit);
        practiceTime = (EditText) findViewById(R.id.trainingEnterTime);
        saveButton = (Button) findViewById(R.id.saveOptionsButton);

        SharedPreferences prefs = getSharedPreferences("my_prefs", 0);
        if(prefs.contains("presentationTime")) {
            presentationTimeValue = prefs.getInt("presentationTime", 0);
            presentationTime.setText(String.format(Integer.toString(presentationTimeValue)));
        }
        if(prefs.contains("practiceTime")) {
            practiceTimeValue = prefs.getInt("practiceTime", 0);
            practiceTime.setText(String.format(Integer.toString(practiceTimeValue)));
        }
        if(prefs.contains("presentationAlerts")){
            presentationAlertsValue = prefs.getBoolean("presentationAlerts", false);
            presentationAlerts.setChecked(presentationAlertsValue);
        }
        if(prefs.contains("practiceAlerts")){
            practiceAlertsValue = prefs.getBoolean("practiceAlerts", false);
            practiceAlerts.setChecked(practiceAlertsValue);
        }
    }

    public void addKeywordScreen(View v){
        Intent intent = new Intent(getApplicationContext(), addKeyWords.class);
        startActivity(intent);
    }

    public void saveOptions(View v){
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("presentationAlerts", presentationAlerts.isChecked());
        edit.putBoolean("practiceAlerts", practiceAlerts.isChecked());
        if(!presentationTime.getText().toString().equals("")) {
            edit.putInt("presentationTime", Integer.parseInt(presentationTime.getText().toString()));
        }
        if(!practiceTime.getText().toString().equals("")){
            edit.putInt("practiceTime", Integer.parseInt(practiceTime.getText().toString()));
        }
        edit.apply();
        Toast.makeText(this, "Opties opgeslagen", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
        startActivity(intent);
    }

}
