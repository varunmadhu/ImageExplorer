package com.example.android23.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android23.model.*;
import com.example.android23.R;

import java.util.ArrayList;

public class moveController extends AppCompatActivity{
    private ListView albumListView;
    private TextView selectedAlbumTextView;
    private Button movePhotoButton, backButton;
    private ArrayList<String> albumList;
    private String selectedAlbum;
    public String getSelectedAlbumString(){
        return selectedAlbum;
    }

    private Photo photoToMove = albumController.getSelectedPhoto();
    private Album originalAlbum = homeController.getSelectedAlbum();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_photo);

        albumListView = findViewById(R.id.albumListView2);
        selectedAlbumTextView = findViewById(R.id.selectedAlbumTextView2);
        movePhotoButton = findViewById(R.id.moveBtn);
        backButton = findViewById(R.id.back_button);

        albumList = new ArrayList<>();

        for(int i = 0; i<homeController.getprimaryUser().getAlbums().size(); i++){
            albumList.add(homeController.getprimaryUser().getAlbums().get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumList);
        albumListView.setAdapter(adapter);
        albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAlbum = albumList.get(position);
                selectedAlbumTextView.setText("Selected Item: " + selectedAlbum);

            }
        });

        movePhotoButton.setOnClickListener(v -> {
            
            if(getSelectedAlbumString().equals(originalAlbum.getName())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("Cannot move to same album");
                builder.setPositiveButton("OK", null);
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                for(int i = 0; i<homeController.getprimaryUser().getAlbums().size(); i++){
                    if(selectedAlbum.equals(homeController.getprimaryUser().getAlbums().get(i).getName())){
                        homeController.getprimaryUser().getAlbums().get(i).addPhoto(photoToMove);
                        break;
                    }
                }
                originalAlbum.deletePhoto(photoToMove);
                Toast.makeText(moveController.this, "Photo moved successfully", Toast.LENGTH_SHORT).show();

            }
        });

        backButton.setOnClickListener(v -> {
            
            Intent intent = new Intent(moveController.this, homeController.class);
            startActivity(intent);
        });
    }

}
