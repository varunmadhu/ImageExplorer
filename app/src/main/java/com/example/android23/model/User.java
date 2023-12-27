package com.example.android23.model;

import java.util.ArrayList;
import java.util.List;

public class User{

    public String name;
	private List<Album> albums;

    public User(String name) {
		this.name = name;
		albums = new ArrayList<>();
	}

    public String getName() {
		return name;
	}

    public List<Album> getAlbums() {
		return albums;
	}

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public void addAlbum(Album album) {
		albums.add(album);
	}

    public void removeAlbum(Album album) {
		albums.remove(album);
	}

}
