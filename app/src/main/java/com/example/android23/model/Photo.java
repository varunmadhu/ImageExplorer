package com.example.android23.model;

import android.net.Uri;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Photo{

    private String name;
	private String caption;
	private String path;
	private LocalDateTime date;
	private List<Tag> tags;

    //private Bitmap bitmap;

    private Uri photoUri;

    public Photo() {

    }

    public Photo(String name, String path, LocalDateTime date) {
		this.name = name;
		this.path = path;
		this.date = date;
		this.tags = new ArrayList<>();
	}

    public Photo(Uri photoUri){
        //this.bitmap = bitmap;
        this.photoUri = photoUri;
        this.tags = new ArrayList<>();
    }

    public String getName() {
		return name;
	}

    public Uri getPhotoUri() {
        return photoUri;
    }

    public List<Tag> getTags() {
		return tags;
	}

    public void addTag(String name, String value) {
		Tag tag = new Tag(name,value);
		tags.add(tag);
	}

    public void removeTag(Tag tag) {
		tags.remove(tag);
		tag = null;
	}

	public boolean subStringChecker(Tag t1, Tag t2) {
		String str1 = t1.getValue().toLowerCase();
		String str2 = t2.getValue().toLowerCase();
		if (str1.contains(str2)) {
			return true;
		}
		return false;
	}

    public boolean compareTags(Tag t1, Tag t2) {
		if (t1.getType().equals(t2.getType())&&t1.getValue().equals(t2.getValue())) {
			return true;
		}
		else {
			return subStringChecker(t1, t2);
		}
	}

}
