package com.example.android23.model;

import java.util.ArrayList;
import java.util.List;


public class Album{

    private String name;
    private List<Photo> photos;
    private int photoCount;

    public Album(String name) {
		this.name = name;
		this.photos = new ArrayList<>();
		this.photoCount = photos.size();
	}

    public void setName(String name) {
		this.name = name;
	}

    public String getName() {
        return name;
    }

    public String toString() {
		return name;
	}

    public void setPhotoList(List<Photo> photos) {
		this.photos = photos;
	}

    public List<Photo> getPhotoList() {
		return photos;
	}

    public int getPhotoCount() {
		return photoCount;
	}

    public void setPhotoCount(int n) {
		this.photoCount = n;
	}

    public void addPhoto(Photo photo) {
		photos.add(photo);
		this.photoCount++;
	}

    public void deletePhoto(Photo photo) {
		photos.remove(photo);
		this.photoCount--;
	}
}
