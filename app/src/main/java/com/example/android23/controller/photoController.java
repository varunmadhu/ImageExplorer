package com.example.android23.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android23.model.*;
import com.example.android23.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class photoController extends AppCompatActivity{
    private ImageView photoImageView;
    private Button previousPhotoButton, nextPhotoButton, addTagButton, deleteTagButton, backButton, movePhotoButton;
    private Photo displayedPhoto = albumController.getSelectedPhoto();

    private ListView tagListView;

    private int currPhotoIndex;

    private ArrayList<String> tagsList;

    private String selectedTag;

    List<Photo> photoList = albumController.photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_photo);

        currPhotoIndex = photoList.indexOf(displayedPhoto);

        tagsList = new ArrayList<>();

        for(int i = 0; i< displayedPhoto.getTags().size(); i++){
            String tag = displayedPhoto.getTags().get(i).getType() + " " + displayedPhoto.getTags().get(i).getValue();
            tagsList.add(tag);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagsList);

        photoImageView = findViewById(R.id.photoImageView);
        previousPhotoButton = findViewById(R.id.previousBtn);
        nextPhotoButton = findViewById(R.id.nextBtn);
        addTagButton = findViewById(R.id.addTagBtn);
        deleteTagButton = findViewById(R.id.deleteTagBtn);
        backButton = findViewById(R.id.backBtn);
        movePhotoButton = findViewById(R.id.moveBtn);
        tagListView = findViewById(R.id.tagListView);
        tagListView.setAdapter(adapter);

        Uri imageUri = displayedPhoto.getPhotoUri();
        photoImageView.setImageURI(imageUri);

        Glide.with(this)
                .load(imageUri)
                .into(photoImageView);


        tagListView.setBackgroundColor(getResources().getColor(android.R.color.white));
        tagListView.setVerticalScrollBarEnabled(true);

        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedTag2 = (String) adapterView.getItemAtPosition(i);
                selectedTag = selectedTag2;
            }
        });

        previousPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (currPhotoIndex > 0) {
                    currPhotoIndex--;
                    updateDisplayedPhoto();
                } else {
                    Toast.makeText(photoController.this, "No previous photo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currPhotoIndex < photoList.size() - 1) {
                    currPhotoIndex++;
                    updateDisplayedPhoto();
                } else {
                    Toast.makeText(photoController.this, "No next photo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(photoController.this);
                builder.setTitle("Add Tag");

                final EditText tagValueEditText = new EditText(photoController.this);
                tagValueEditText.setHint("Person or Location");

                final Spinner tagTypeSpinner = new Spinner(photoController.this);
                String[] tagTypes = new String[]{"Person", "Location"};
                ArrayAdapter<String> tagTypeAdapter = new ArrayAdapter<>(photoController.this, android.R.layout.simple_spinner_dropdown_item, tagTypes);
                tagTypeSpinner.setAdapter(tagTypeAdapter);

                LinearLayout layout = new LinearLayout(photoController.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(tagTypeSpinner);
                layout.addView(tagValueEditText);
                builder.setView(layout);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tagKey = tagTypeSpinner.getSelectedItem().toString();
                        String tagValue = tagValueEditText.getText().toString();
                        displayedPhoto.addTag(tagKey, tagValue);
                        String tagToDisplay = tagKey + " " + tagValue;

                        tagsList.add(tagToDisplay);
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) tagListView.getAdapter();
                        //adapter.clear();
                        //adapter.addAll(tagToDisplay);
                        adapter.notifyDataSetChanged();


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
            }
        });

        deleteTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tagToBeDeleted = selectedTag;
                String[] parts = tagToBeDeleted.split(" ");
                Tag tagtoDelete = new Tag(parts[0], parts[1]);

                if (tagToBeDeleted!= null){
                    for(int i = 0; i<displayedPhoto.getTags().size(); i++){
                        if(displayedPhoto.compareTags(tagtoDelete, displayedPhoto.getTags().get(i))){
                            displayedPhoto.removeTag(tagtoDelete);
                            break;
                        }
                    }
                    tagsList.remove(tagToBeDeleted);
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) tagListView.getAdapter();
                    adapter.clear();
                    adapter.addAll(tagsList);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle back button click
                Intent intent = new Intent(photoController.this, albumController.class);
                startActivity(intent);
            }
        });

        movePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(photoController.this, moveController.class);
                startActivity(intent);
            }
        });
    }

    private void updateDisplayedPhoto() {
        displayedPhoto = photoList.get(currPhotoIndex);
        Uri imageUri = displayedPhoto.getPhotoUri();
        Glide.with(this)
                .load(imageUri)
                .into(photoImageView);
        updateTagsList();
    }

    private void updateTagsList() {
        tagsList.clear();
        for (int i = 0; i < displayedPhoto.getTags().size(); i++) {
            String tag = displayedPhoto.getTags().get(i).getType() + " " + displayedPhoto.getTags().get(i).getValue();
            tagsList.add(tag);
        }
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) tagListView.getAdapter();
        adapter.notifyDataSetChanged();
    }


}
