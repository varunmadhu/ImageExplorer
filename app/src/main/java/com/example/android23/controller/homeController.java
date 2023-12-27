package com.example.android23.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android23.model.*;
import com.example.android23.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


import android.content.Context;
import android.content.SharedPreferences;

public class homeController extends AppCompatActivity{

    private ListView albumListView;
    private TextView selectedAlbumTextView;
    private Button createAlbumBtn;
    private Button deleteAlbumBtn;
    private Button openAlbumBtn;
    private Button renameAlbumBtn;
    private Button searchPhotosBtn;

    private static ArrayList<String> albumList;

    private String selectedAlbumString;
    private static Album selectedAlbum;


    public String getSelectedAlbumString(){
        return selectedAlbumString;
    }

    public static Album getSelectedAlbum(){
        return selectedAlbum;
    }

    public void setselectedAlbum(String album){
        for(int i = 0; i<primaryUser.getAlbums().size();i++){
            if(album.equals(primaryUser.getAlbums().get(i).getName())){
                selectedAlbum = primaryUser.getAlbums().get(i);
                break;
            }
        }
    }

    public static User primaryUser = new User("user");

    public static User getprimaryUser(){
        return primaryUser;
    }


    @Override
    protected void onPause() {
        super.onPause();
        saveAlbums(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlbums();
        updateAlbumList();
    }

    private void updateAlbumList() {
        albumList.clear();
        for (int i = 0; i < primaryUser.getAlbums().size(); i++) {
            albumList.add(primaryUser.getAlbums().get(i).getName());
        }
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) albumListView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    public static void saveAlbums(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(albumList);
        editor.putString("albumList", json);
        editor.apply();
    }

    private void loadAlbums() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("album", null);
        Type type = new TypeToken<ArrayList<Album>>() {}.getType();
        ArrayList<Album> loadedAlbums = gson.fromJson(json, type);

        if (loadedAlbums != null) {
            primaryUser.setAlbums(loadedAlbums);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        albumListView = findViewById(R.id.albumListView);
        selectedAlbumTextView = findViewById(R.id.selectedAlbumTextView);
        createAlbumBtn = findViewById(R.id.createAlbumBtn);
        deleteAlbumBtn = findViewById(R.id.deleteAlbumBtn);
        renameAlbumBtn = findViewById(R.id.renameAlbumBtn);
        openAlbumBtn = findViewById(R.id.openAlbumBtn);
        searchPhotosBtn = findViewById(R.id.searchPhotosBtn);

        albumList = new ArrayList<>();

        for(int i = 0; i<primaryUser.getAlbums().size(); i++){
            albumList.add(primaryUser.getAlbums().get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumList);
        albumListView.setAdapter(adapter);


        albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAlbumString = albumList.get(position);
                selectedAlbumTextView.setText("Selected Album: " + selectedAlbumString);

            }
        });


        createAlbumBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(homeController.this);
                builder.setTitle("Enter Album Name:");

                final EditText input = new EditText(homeController.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String albumName = input.getText().toString();
                        Album newAlbum = new Album(albumName);
                        primaryUser.addAlbum(newAlbum);
                        albumList.add(albumName);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                saveAlbums(homeController.this);
                updateAlbumList();
            }

        });

        deleteAlbumBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String deletingAlbum = getSelectedAlbumString();

                if(deletingAlbum != null){
                    for(int i = 0; i<primaryUser.getAlbums().size(); i++){
                        if(deletingAlbum.compareTo(primaryUser.getAlbums().get(i).getName()) == 0){
                            primaryUser.getAlbums().remove(i);
                        }
                    }
                    albumList.remove(deletingAlbum);
                    adapter.notifyDataSetChanged();
                }

                saveAlbums(homeController.this);
                updateAlbumList();
            }

        });

        renameAlbumBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(homeController.this);
                builder.setTitle("Enter album name:");

                final EditText input = new EditText(homeController.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String albumName = input.getText().toString();
                        for(int i = 0; i<primaryUser.getAlbums().size(); i++){
                            if(albumName.compareTo(primaryUser.getAlbums().get(i).getName())==0){
                                primaryUser.getAlbums().get(i).setName(albumName);
                                break;
                            }
                        }
                        String deletingAlbum = getSelectedAlbumString();
                        albumList.remove(deletingAlbum);
                        albumList.add(albumName);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }

        });



        openAlbumBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveAlbums(homeController.this);
                for(int i = 0; i<primaryUser.getAlbums().size(); i++){
                    if(selectedAlbumString.equals(primaryUser.getAlbums().get(i).getName())){
                        selectedAlbum = primaryUser.getAlbums().get(i);
                        break;
                    }
                }
                albumController.currAlbum = selectedAlbum;
                Intent intent = new Intent(homeController.this, albumController.class);
                startActivity(intent);
            }

        });

        searchPhotosBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(homeController.this, searchInputController.class);
                startActivity(intent);
            }

        });
    }
}
