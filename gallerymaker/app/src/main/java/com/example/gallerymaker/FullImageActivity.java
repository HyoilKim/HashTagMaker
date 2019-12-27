package com.example.gallerymaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.widget.ImageView.ScaleType;
import androidx.appcompat.app.AppCompatActivity;
public class FullImageActivity extends AppCompatActivity {
    PhotoViewAttacher mAttacher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById((R.id.full_image_view));
        mAttacher = new PhotoViewAttacher(imageView);
        imageView.setImageResource(imageAdapter.mThumbIds[position]);
    }

}