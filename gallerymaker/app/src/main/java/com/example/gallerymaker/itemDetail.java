package com.example.gallerymaker;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class itemDetail extends AppCompatActivity {

    private TextView name;
    private TextView phone_number;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        name = (TextView)findViewById(R.id.detail_name);
        phone_number = (TextView)findViewById(R.id.detail_phoneNumber);

        name.setText(intent.getStringExtra("name"));
        phone_number.setText(intent.getStringExtra("phone_number"));

    }
}
