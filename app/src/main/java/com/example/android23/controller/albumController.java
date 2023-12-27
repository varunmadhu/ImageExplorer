package com.example.android23.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.android23.model.*;
import com.example.android23.R;

public class albumController extends AppCompatActivity{
    
    private GridView gridView;
    public static List<Photo> photoList;
    private PhotoAdapter photoAdapter;
    private Album albumToView = homeController.getSelectedAlbum();

    public static Photo selectedPhoto;

    public static Album currAlbum;

    public static Photo getSelectedPhoto(){
        return selectedPhoto;
    }
    public static void setSelectedPhoto(Photo photo){
        selectedPhoto = photo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_display);

        currAlbum = homeController.getSelectedAlbum();

        photoList = new ArrayList<>();
        photoList = currAlbum.getPhotoList();

        gridView = findViewById(R.id.gridView);
        photoAdapter = new PhotoAdapter(albumController.this, photoList);
        gridView.setAdapter(photoAdapter);

        photoAdapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPhoto = (Photo) parent.getItemAtPosition(position);
            }
        });

        Button addPhotoBtn = findViewById(R.id.addPhotoBtn);
        Button deletPhotoBtn = findViewById(R.id.deletePhotoBtn);
        Button displayPhotoBtn = findViewById(R.id.displayPhotoBtn);
        Button backButton = findViewById(R.id.backButton);


        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select a Picture"), 1);
            }
        });
        deletPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeController.getSelectedAlbum().deletePhoto(selectedPhoto);
                photoList.remove(selectedPhoto);
                photoAdapter.notifyDataSetChanged();
            }
        });

        displayPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(albumController.this, photoController.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(albumController.this, homeController.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri imageUri = data.getData();
                Photo photo = new Photo(imageUri);
                currAlbum.addPhoto(photo);
                photoAdapter.notifyDataSetChanged();
                Toast.makeText(albumController.this, "Photo Added", Toast.LENGTH_SHORT).show();
                homeController.saveAlbums(albumController.this);
            }
        }
    }

}
