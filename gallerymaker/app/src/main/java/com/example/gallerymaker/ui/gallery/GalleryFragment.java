package com.example.gallerymaker.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.gallerymaker.FullImageActivity;
import com.example.gallerymaker.ImageAdapter;
import com.example.gallerymaker.R;
public class GalleryFragment extends Fragment {
    public static ImageAdapter imageAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.grid_layout, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        imageAdapter = new ImageAdapter(getActivity());

        imageAdapter.pictures.add(R.drawable.pic_1);
        imageAdapter.pictures.add(R.drawable.pic_2);
        imageAdapter.pictures.add(R.drawable.pic_3);
        imageAdapter.pictures.add(R.drawable.pic_4);
        imageAdapter.pictures.add(R.drawable.pic_5);
        imageAdapter.pictures.add(R.drawable.pic_6);
        imageAdapter.pictures.add(R.drawable.pic_7);
        imageAdapter.pictures.add(R.drawable.pic_8);
        imageAdapter.pictures.add(R.drawable.pic_9);
        imageAdapter.pictures.add(R.drawable.pic_10);
        imageAdapter.pictures.add(R.drawable.pic_11);
        imageAdapter.pictures.add(R.drawable.pic_12);
        imageAdapter.pictures.add(R.drawable.pic_13);
        imageAdapter.pictures.add(R.drawable.pic_14);
        imageAdapter.pictures.add(R.drawable.pic_15);
        imageAdapter.pictures.add(R.drawable.pic_16);
        imageAdapter.pictures.add(R.drawable.pic_17);
        imageAdapter.pictures.add(R.drawable.pic_18);
        imageAdapter.pictures.add(R.drawable.pic_19);
        imageAdapter.pictures.add(R.drawable.pic_20);
        imageAdapter.pictures.add(R.drawable.pic_21);



        // Instance of ImageAdapter Class
        gridView.setAdapter(imageAdapter);

        /**
         * On Click event for Single Gridview Item
         * */
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Intent i = new Intent(getActivity().getApplicationContext(), FullImageActivity.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });
        return view;
    }
}