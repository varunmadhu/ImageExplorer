package com.example.android23.controller;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import com.example.android23.R;
import com.example.android23.model.*;

public class searchOutputController extends AppCompatActivity{
    
    private GridView photoGridView;
    public static List<Photo> searchOutputList;
    private PhotoAdapter photoAdapter;
    private Button backButton;
    private Photo selectedResultPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_output);

        photoGridView = findViewById(R.id.photoGridView);
        backButton = findViewById(R.id.backButton);

        photoAdapter = new PhotoAdapter(searchOutputController.this, searchOutputList);
        photoGridView.setAdapter(photoAdapter);

        photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedResultPhoto = (Photo) parent.getItemAtPosition(position);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchOutputController.this, searchInputController.class);
                startActivity(intent);
            }
        });

    }
}
