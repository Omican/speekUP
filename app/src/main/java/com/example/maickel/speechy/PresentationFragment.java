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

import java.io.File;
import java.util.Set;

/**
 * Created by Maickel on 1/9/2017.
 */

public class PresentationFragment extends Fragment {
    private Switch presentationAlerts;
    private EditText presentationTime;
    private int presentationTimeValue;
    private Boolean presentationAlertsValue;
    private Boolean presentationKeywordsRadioValue;
    private Boolean presentationMostSpokenRadioValue;
    private RadioButton presentationKeywordsRadio;
    private RadioButton presentationMostSpokenRadio;
    private CardView saveCard;
    private boolean keyWordsSet;

    public PresentationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_presentation, container, false);
        presentationAlerts = (Switch) view.findViewById(R.id.togglePresentationAlerts);
        presentationTime = (EditText) view.findViewById(R.id.presentationTimeEdit);
        presentationKeywordsRadio = (RadioButton) view.findViewById(R.id.presentationShowKeywordRadio);
        presentationMostSpokenRadio = (RadioButton) view.findViewById(R.id.presentationShowMostRepeatedWordsRadio);
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
        if(presentationKeywordsRadio.isChecked()){
            if(keyWordsSet){
               applyPreferences();
            } else {
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
        edit.putBoolean("presentationShowKeywords", presentationKeywordsRadio.isChecked());
        edit.putBoolean("presentationMostSpokenWords", presentationMostSpokenRadio.isChecked());
        edit.putBoolean("presentationAlerts", presentationAlerts.isChecked());
        edit.putBoolean("presentationOptionsSet", true);
        if (!presentationTime.getText().toString().equals("")) {
            edit.putInt("presentationTime", Integer.parseInt(presentationTime.getText().toString()));
        }
        edit.apply();
        Toast.makeText(getActivity().getApplicationContext(), "Opties opgeslagen", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(), MainScreen.class);
        startActivity(intent);
    }

    private void fillSetFields(){
        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", 0);
        if(prefs.contains("presentationTime")) {
            presentationTimeValue = prefs.getInt("presentationTime", 0);
            presentationTime.setText(String.format(Integer.toString(presentationTimeValue)));
        }
        if(prefs.contains("presentationAlerts")){
            presentationAlertsValue = prefs.getBoolean("presentationAlerts", false);
            presentationAlerts.setChecked(presentationAlertsValue);
        }
        if(prefs.contains("presentationKeywords")){
            presentationKeywordsRadioValue = prefs.getBoolean("presentationShowKeywords", false);
            presentationKeywordsRadio.setChecked(presentationKeywordsRadioValue);
        }
        if(prefs.contains("presentationMostSpokenWords")){
            presentationMostSpokenRadioValue = prefs.getBoolean("presentationMostSpokenWords", false);
            presentationMostSpokenRadio.setChecked(presentationMostSpokenRadioValue);
        }
    }
}
