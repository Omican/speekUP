package com.example.maickel.speechy;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maickel on 1/11/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private ArrayList<String> mDataSet;
    private ArrayList<ArrayList<String>> data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title;
        public ViewHolder(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.recordCardTitle);
            this.view = v;
        }
    }

    public CustomAdapter(ArrayList<String> dataSet, ArrayList<ArrayList<String>> data) {
        mDataSet = dataSet;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_recordingcard, viewGroup, false);
        return new ViewHolder(v);
    }

    public void showText(){

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        TextView textViewTitle = viewHolder.title;
        textViewTitle.setText(mDataSet.get(position));
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), ShowText.class);
                intent.putExtra("SpokenText", data.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
