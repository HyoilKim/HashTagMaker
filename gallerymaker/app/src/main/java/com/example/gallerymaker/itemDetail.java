package com.example.gallerymaker;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class itemDetail extends AppCompatActivity {

    private TextView name;
    private TextView phone_number;
    private ImageView img;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detailview);

        Intent intent = getIntent();
        name = (TextView)findViewById(R.id.detail_name);
        phone_number = (TextView)findViewById(R.id.detail_phoneNumber);
        img = (ImageView) findViewById(R.id.detail_img);

        Log.d("detail_name", intent.getStringExtra("name"));
        name.setText(intent.getStringExtra("name"));
        phone_number.setText(intent.getStringExtra("phone_number"));
        img.setImageResource(intent.getIntExtra("img", 0));

    }
}
