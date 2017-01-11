package com.example.maickel.speechy;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Maickel on 1/11/2017.
 */

public class RecordingCardView extends CardView {
    private RelativeLayout layout;
    private TextView cardTitle;

    public RecordingCardView(Context context, String[] data, String title) {
        super(context);

        layout = new RelativeLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        layout.setLayoutParams(layoutParams);

        this.addView(layout);

        cardTitle = new TextView(context);
        cardTitle.setText(title);
        cardTitle.setGravity(Gravity.CENTER);

        this.layout.addView(cardTitle);
    }
}
