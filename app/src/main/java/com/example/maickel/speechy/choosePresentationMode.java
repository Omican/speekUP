package com.example.maickel.speechy;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;


public class choosePresentationMode extends AppCompatActivity {
    private CardView presentationCard;
    private CardView trainingCard;
    private final int PERMISSIONS_RECORD_AUDIO = 0;
    private boolean permissionGranted;
    private boolean presentationOptionsSet;
    private boolean practiceOptionsSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_presentation_mode);

        presentationCard = (CardView) findViewById(R.id.presentationCard);
        trainingCard = (CardView) findViewById(R.id.trainingCard);



        presentationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPresentationMode(v);
            }
        });

        trainingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPracticeMode(v);
            }
        });

        SharedPreferences prefs = getSharedPreferences("my_prefs", 0);

        if(prefs.contains("presentationOptionsSet")){
            presentationOptionsSet = true;
        } else {
            presentationOptionsSet = false;
        }

        if(prefs.contains("practiceOptionsSet")){
            practiceOptionsSet = true;
        } else{
            practiceOptionsSet = false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSIONS_RECORD_AUDIO);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;

                } else {
                    permissionGranted = false;
                }
                return;
            }
        }
    }

    public void openPresentationMode(View v){
        permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if(presentationOptionsSet) {
            if (permissionGranted) {
                Intent intent = new Intent(getApplicationContext(), Presentation.class);
                SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("presentationMode", "presentationMode");
                edit.apply();
                startActivity(intent);
            } else {
                openPermissionsDialog();
            }
        } else{
            openOptionsSetDialog();
        }
    }

    public void openPracticeMode(View v){
        permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if(practiceOptionsSet) {
            if (permissionGranted) {
                Intent intent = new Intent(getApplicationContext(), Presentation.class);
                SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("presentationMode", "practiceMode");
                edit.apply();
                startActivity(intent);
            } else {
                openPermissionsDialog();
            }
        }else {
            openOptionsSetDialog();
        }
    }

    private void openOptionsSetDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Opties niet ingesteld")
                .setMessage("Er zijn voor deze modus nog geen opties ingesteld. Druk op OK om opties in te stellen.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(getApplicationContext(), Options.class);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void openPermissionsDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Permissies")
                .setMessage("Om te deze app te gebruiken is er permissie nodig om de microfoon te gebruiken. Click op OK om naar de instellingen van deze app te gaan.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
