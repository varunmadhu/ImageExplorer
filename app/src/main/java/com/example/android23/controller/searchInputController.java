package com.example.android23.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Spinner;


import androidx.appcompat.app.AppCompatActivity;

import com.example.android23.model.*;
import com.example.android23.R;

import java.util.ArrayList;
import java.util.List;

public class searchInputController extends AppCompatActivity{
    
    private Spinner tagTypeSpinner1;
    private EditText tagValueEditText1;

    private Spinner tagTypeSpinner2;
    private EditText tagValueEditText2;

    private RadioGroup radioGroup;
    private RadioButton andButton, orButton, singleButton;

    private ListView tagsListView;
    // need to implement the rest of the xml elements

    private ArrayList<String> tagsList = new ArrayList<>();

    private String selectedTag;

    public List<Photo> similarPhotoList;

    public String getSelectedTag(){
        return selectedTag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_input);

        // int selectedId = radioGroup.getCheckedRadioButtonId();
        andButton = (RadioButton) findViewById(R.id.radio_and);
        orButton = (RadioButton) findViewById(R.id.radio_or);
        singleButton = (RadioButton) findViewById(R.id.radio_single_tag_value);

        tagTypeSpinner1 = findViewById(R.id.dropdown1);
        tagValueEditText1 = findViewById(R.id.tag_value1); // having issues with this when running app

        tagTypeSpinner2 = findViewById(R.id.dropdown2);
        tagValueEditText2 = findViewById(R.id.tag_value2);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        tagsListView = findViewById(R.id.tagsListView);

        ArrayAdapter<CharSequence> spinneradapter1 = ArrayAdapter.createFromResource(this, R.array.tag_types, android.R.layout.simple_spinner_item);
        spinneradapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagTypeSpinner1.setAdapter(spinneradapter1);

        ArrayAdapter<CharSequence> spinneradapter2 = ArrayAdapter.createFromResource(this, R.array.tag_types, android.R.layout.simple_spinner_item);
        spinneradapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagTypeSpinner2.setAdapter(spinneradapter2);

        tagsList.clear();

        User user = homeController.getprimaryUser();
        List<Album> albumList = user.getAlbums();
        for (int i = 0; i < albumList.size(); i++) {
            Album album = albumList.get(i);
            List<Photo> photos = album.getPhotoList();
            for (int j = 0; j < photos.size(); j++) {
                Photo photo = photos.get(j);
                List<Tag> tags = photo.getTags();
                for (int k = 0; k < tags.size(); k++) {
                    tagsList.add(tags.get(k).getType() + " " + tags.get(k).getValue());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagsList);

        tagValueEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                
                ArrayList<String> filteredData = new ArrayList<>();
                for (String item : tagsList) {
                    if (item.toLowerCase().startsWith(s.toString().toLowerCase())) {
                        filteredData.add(item);
                    }
                }
                
                adapter.clear();
                adapter.addAll(filteredData);
            }
        });
        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Tag = (String) parent.getItemAtPosition(position);
                selectedTag = Tag;
            }
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchInputController.this, homeController.class);
                startActivity(intent);
            }
        });

        Button searchButton = findViewById(R.id.search_button);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedKey1 = tagTypeSpinner1.getSelectedItem().toString();
                String inputValue1 = tagValueEditText1.getText().toString();
                String selectedKey2 = tagTypeSpinner2.getSelectedItem().toString();
                String inputValue2 = tagValueEditText2.getText().toString();
                similarPhotoList = new ArrayList<>();

                if (searchOutputController.searchOutputList == null) {
                    searchOutputController.searchOutputList = new ArrayList<>();
                } else {
                    searchOutputController.searchOutputList.clear();
                }

                if (singleButton.isChecked()) {
                    if (!inputValue1.isEmpty()) {
                        Tag tempTag = new Tag(selectedKey1, inputValue1);
                        User user1 = homeController.getprimaryUser();
                        List<Album> albumList1 = user.getAlbums();

                        for (int i = 0; i < albumList1.size(); i++) {
                            Album album1 = albumList1.get(i);
                            List<Photo> photos1 = album1.getPhotoList();
                            for (int j = 0; j < photos1.size(); j++) {
                                Photo photo1 = photos1.get(j);
                                List<Tag> tags1 = photo1.getTags();
                                for (int k = 0; k < tags1.size(); k++) {
                                    if (photo1.compareTags(tags1.get(k), tempTag)) {
                                        similarPhotoList.add(photo1);
                                    }
                                }
                            }
                        }

                    }
                }

                if (andButton.isChecked()) {
                    if (!inputValue1.isEmpty()&&!inputValue2.isEmpty()) {
                        Tag tempTag1 = new Tag(selectedKey1, inputValue1);
                        Tag tempTag2 = new Tag(selectedKey2, inputValue2);
                        User user1 = homeController.getprimaryUser();
                        List<Album> albumList1 = user.getAlbums();

                        for (int i = 0; i < albumList1.size(); i++) {
                            Album album1 = albumList1.get(i);
                            List<Photo> photos1 = album1.getPhotoList();
                            for (int j = 0; j < photos1.size(); j++) {
                                Photo photo1 = photos1.get(j);
                                boolean tag1 = false;
                                boolean tag2 = false;
                                List<Tag> tags1 = photo1.getTags();
                                for (int k = 0; k < tags1.size(); k++) {
                                    if (photo1.compareTags(tags1.get(k), tempTag1)) {
                                        tag1 = true;
                                    }
                                    if (photo1.compareTags(tags1.get(k), tempTag2)) {
                                        tag2 = true;
                                    }
                                }
                                if (tag1&&tag2) {similarPhotoList.add(photo1);}
                            }
                        }

                    }
                }

                if (orButton.isChecked()) {
                    if (!inputValue1.isEmpty()&&!inputValue2.isEmpty()) {
                        Tag tempTag1 = new Tag(selectedKey1, inputValue1);
                        Tag tempTag2 = new Tag(selectedKey2, inputValue2);
                        User user1 = homeController.getprimaryUser();
                        List<Album> albumList1 = user.getAlbums();

                        for (int i = 0; i < albumList1.size(); i++) {
                            Album album1 = albumList1.get(i);
                            List<Photo> photos1 = album1.getPhotoList();
                            for (int j = 0; j < photos1.size(); j++) {
                                Photo photo1 = photos1.get(j);
                                boolean tag1 = false;
                                boolean tag2 = false;
                                List<Tag> tags1 = photo1.getTags();
                                for (int k = 0; k < tags1.size(); k++) {
                                    if (photo1.compareTags(tags1.get(k), tempTag1)) {
                                        tag1 = true;
                                    }
                                    if (photo1.compareTags(tags1.get(k), tempTag2)) {
                                        tag2 = true;
                                    }
                                }
                                if (tag1||tag2) {similarPhotoList.add(photo1);}
                            }
                        }

                    }
                }
                

                searchOutputController.searchOutputList = similarPhotoList;
                Intent intent = new Intent(searchInputController.this, searchOutputController.class);
                startActivity(intent);
            }
        });
    }
}


