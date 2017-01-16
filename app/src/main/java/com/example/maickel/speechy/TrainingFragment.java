package com.example.maickel.speechy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Maickel on 1/9/2017.
 */

public class TrainingFragment extends Fragment {
    private Switch practiceAlerts;
    private EditText practiceTime;
    private int practiceTimeValue;
    private Boolean practiceAlertsValue;
    private Boolean practiceKeyswordsRadioValue;
    private Boolean practiceMostSpokenRadioValue;
    private RadioButton practiceKeywordsRadio;
    private RadioButton practiceMostSpokenRadio;
    private CardView saveCard;
    private boolean keyWordsSet;
    public TrainingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_training, container, false);
        practiceAlerts = (Switch) view.findViewById(R.id.toggleTrainingAlerts);
        practiceTime = (EditText) view.findViewById(R.id.trainingEnterTime);
        practiceKeywordsRadio = (RadioButton) view.findViewById(R.id.practiceKeywordRadio);
        practiceMostSpokenRadio = (RadioButton) view.findViewById(R.id.practiceMostSpokenWordsRadio);
        saveCard = (CardView) view.findViewById(R.id.saveCard);

        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOptions(v);
            }
        });

        fillSetFields();

        return view;
    }


    public void saveOptions(View v){
        SharedPreferences preferences = getActivity().getSharedPreferences("my_prefs", 0);
        if(preferences.contains("KeyWords")){
            keyWordsSet = true;
        } else{
            keyWordsSet = false;
        }
        if(practiceKeywordsRadio.isChecked()){
            if(keyWordsSet){
                applyPreferences();
            }else{
                openKeywordsSetDialog();
            }
        }else {
            applyPreferences();
        }
    }

    private void openKeywordsSetDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle("Geen keywords gevonden")
                .setMessage("Er zijn nog geen keywords toegevoegd. Click op OK om toe te voegen")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Create new fragment and transaction
                        KeywordFragment newFragment = new KeywordFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).commit();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void applyPreferences(){
        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putBoolean("practiceShowKeywords", practiceKeywordsRadio.isChecked());
        edit.putBoolean("practiceMostSpokenWords", practiceMostSpokenRadio.isChecked());

        edit.putBoolean("practiceOptionsSet", true);

        edit.putBoolean("practiceAlerts", practiceAlerts.isChecked());

        if (!practiceTime.getText().toString().equals("")) {
            edit.putInt("practiceTime", Integer.parseInt(practiceTime.getText().toString()));
        }
        edit.apply();
        Toast.makeText(getActivity().getApplicationContext(), "Opties opgeslagen", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(), MainScreen.class);
        startActivity(intent);
    }

    private void fillSetFields(){
        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", 0);
        if(prefs.contains("practiceTime")) {
            practiceTimeValue = prefs.getInt("practiceTime", 0);
            practiceTime.setText(String.format(Integer.toString(practiceTimeValue)));
        }
        if(prefs.contains("practiceAlerts")){
            practiceAlertsValue = prefs.getBoolean("practiceAlerts", false);
            practiceAlerts.setChecked(practiceAlertsValue);
        }
        if(prefs.contains("practiceKeywords")){
            practiceKeyswordsRadioValue = prefs.getBoolean("practiceShowKeywords", false);
            practiceKeywordsRadio.setChecked(practiceKeyswordsRadioValue);
        }
        if(prefs.contains("practiceMostSpokenWords")){
            practiceMostSpokenRadioValue = prefs.getBoolean("practiceMostSpokenWords", false);
            practiceMostSpokenRadio.setChecked(practiceMostSpokenRadioValue);
        }
    }
}
