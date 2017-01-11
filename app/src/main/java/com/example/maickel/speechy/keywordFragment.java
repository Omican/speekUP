package com.example.maickel.speechy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Maickel on 1/9/2017.
 */

public class KeywordFragment extends Fragment {
    public ArrayList<String> keyWords;
    private EditText keyWord0;
    private EditText keyWord1;
    private EditText keyWord2;
    private EditText keyWord3;
    private EditText keyWord4;
    private Set<String> keyWordsSet;
    private CardView emptyCard;
    private CardView saveCard;

    public KeywordFragment() {
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
        View view = inflater.inflate(R.layout.activity_add_key_words, container, false);
        keyWord0 = (EditText) view.findViewById(R.id.keyWord0);
        keyWord1 = (EditText) view.findViewById(R.id.keyWord1);
        keyWord2 = (EditText) view.findViewById(R.id.keyWord2);
        keyWord3 = (EditText) view.findViewById(R.id.keyWord3);
        keyWord4 = (EditText) view.findViewById(R.id.keyWord4);
        saveCard = (CardView) view.findViewById(R.id.saveKeywordsCard);
        emptyCard = (CardView) view.findViewById(R.id.emptyKeywordsCard);

        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveKeywords(v);
            }
        });
        emptyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText(v);
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", 0);
        keyWordsSet = prefs.getStringSet("KeyWords", new HashSet<String>());
        List<String> keyWordsList = new ArrayList<String>(keyWordsSet);

        for(int i = 0; i < keyWordsList.size(); i++){
            if(i == 0){
                keyWord0.setText(keyWordsList.get(i));
            }
            if(i == 1){
                keyWord1.setText(keyWordsList.get(i));
            }
            if(i == 2){
                keyWord2.setText(keyWordsList.get(i));
            }
            if(i == 3){
                keyWord3.setText(keyWordsList.get(i));
            }
            if(i == 4){
                keyWord4.setText(keyWordsList.get(i));
            }
        }
        return view;
    }

    public void saveKeywords (View v){
        keyWords = new ArrayList<>();
        if(!keyWord0.getText().toString().matches("")) {
            keyWords.add(keyWord0.getText().toString().replaceAll("\\s+","").toLowerCase());
        }
        if(!keyWord1.getText().toString().matches("")) {
            keyWords.add(keyWord1.getText().toString().replaceAll("\\s+","").toLowerCase());
        }
        if(!keyWord2.getText().toString().matches("")) {
            keyWords.add(keyWord2.getText().toString().replaceAll("\\s+","").toLowerCase());
        }
        if(!keyWord3.getText().toString().matches("")) {
            keyWords.add(keyWord3.getText().toString().replaceAll("\\s+","").toLowerCase());
        }
        if(!keyWord4.getText().toString().matches("")) {
            keyWords.add(keyWord4.getText().toString().replaceAll("\\s+","").toLowerCase());
        }
        Set keyWordsSet = new HashSet(keyWords);
        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("KeyWords", keyWordsSet);
        edit.apply();
        Intent intent = new Intent(getContext(), MainScreen.class);
        startActivity(intent);
    }

    public void clearText(View v){
        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("KeyWords", null);
        edit.apply();
        startActivity(getActivity().getIntent());
    }

}

