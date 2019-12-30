package com.example.gallerymaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import uk.co.senab.photoview.PhotoViewAttacher;
public class FullImageActivity extends AppCompatActivity {
    PhotoViewAttacher mAttacher;
    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();


        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int position = i.getExtras().getInt("id");
        pager = (ViewPager) findViewById(R.id.pager) ;
        CustomAdapter adapter = new CustomAdapter(getLayoutInflater());
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);

////
//        // Selected image id
//        int position = i.getExtras().getInt("id");
//        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
//        mAttacher = new PhotoViewAttacher(imageView);
////        Log.d( "", ""+ ((MainActivity) MainActivity.context_main).imageAdapter.getCount());
//        imageView.setImageBitmap(GalleryFragment.imageAdapterinfrag.gridviewimages.get(position));
//        imageView.bringToFront();

    }

}