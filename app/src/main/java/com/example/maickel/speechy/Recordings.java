package com.example.maickel.speechy;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class Recordings extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> cardTitles = new ArrayList<>();
    private CustomAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<ArrayList<String>> resultsArray;
    private CardView deleteRecordingCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        deleteRecordingCard = (CardView) findViewById(R.id.deleteRecordingCard);
        deleteRecordingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllRecordings();
            }
        });
        resultsArray = new ArrayList<ArrayList<String>>();
        try {
            File file = new File(getCacheDir(), "SavedPresentations");
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                ObjectInputStream br = new ObjectInputStream(new FileInputStream(files[i]));
                Object readString = br.readObject();
                String[] results = readString.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\.", ":").replaceAll(";", " ").split(", ");

                cardTitles.add(results[results.length - 1]);

                ArrayList<String> resultList = new ArrayList<>();

                for(int j = 0; j < results.length - 1; j++){
                    resultList.add(results[j]);
                }

                resultsArray.add(resultList);
            }
        }catch (FileNotFoundException e){

        } catch (IOException e){

        } catch (ClassNotFoundException e){

        }
        recyclerView = (RecyclerView) findViewById(R.id.recordingRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CustomAdapter(cardTitles, resultsArray);
        recyclerView.setAdapter(adapter);

        try {
            getResults();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getResults() throws IOException, ClassNotFoundException {
        File file = new File(getCacheDir(), "SavedPresentations");
        File[] files = file.listFiles();
        for(int i = 0; i < files.length; i++) {
            ObjectInputStream br = new ObjectInputStream(new FileInputStream(files[i]));
            Object readString = br.readObject();
            String[] results = readString.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\.", ":").replaceAll(";", " ").split(", ");
        }
    }

    public void deleteAllRecordings(){
        new AlertDialog.Builder(this)
                .setTitle("Verwijder Presentaties")
                .setMessage("Weet je zeker dat je alle presentaties wilt verwijderen?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        File makeDir = new File(getCacheDir(), "SavedPresentations");
                        String[] children = makeDir.list();
                        for(int i = 0; i <children.length; i++){
                            new File(makeDir, children[i]).delete();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Presentaties Verwijderd", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
