package com.example.android23.model;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.*;

public class PhotoAdapter extends BaseAdapter{

    private Context context;
    private List<Photo> photoList;



    public PhotoAdapter(Context c, List<Photo> photoList){
        context = c;
        this.photoList = photoList;
    }

    public void displayPhoto(Photo photo, ImageView imageView) {
        Uri photoUri = photo.getPhotoUri();
        Glide.with(imageView.getContext())
                .load(photoUri)
                .into(imageView);
    }

    public int getCount(){
        return photoList.size();
    }

    public Object getItem(int index){
        return photoList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return 0;
    }

    public View getView(int index, View convertView, ViewGroup parent){
        
        ImageView imageView;
        // ImageView imgview = null;

        if(convertView == null){
            imageView = new ImageView(context);

            //TODO: Edit the dimensions.
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        }
        else{
            imageView = (ImageView)convertView;
        }

        Uri photoUri = photoList.get(index).getPhotoUri();
        imageView.setImageURI(photoUri);
        return imageView;
    }
}
