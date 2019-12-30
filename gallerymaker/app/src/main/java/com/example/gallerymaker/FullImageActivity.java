package com.example.gallerymaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gallerymaker.ui.gallery.GalleryFragment;

import uk.co.senab.photoview.PhotoViewAttacher;
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

        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        mAttacher = new PhotoViewAttacher(imageView);
        Log.d( "", ""+ GalleryFragment.imageAdapter.getCount());
//        imageView.setImageResource(GalleryFragment.imageAdapter.pictures.get(position));
        imageView.setImageBitmap(GalleryFragment.imageAdapter.gridviewimages.get(position));
        imageView.bringToFront();
    }

}