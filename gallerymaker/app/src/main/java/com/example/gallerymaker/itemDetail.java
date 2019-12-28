package com.example.gallerymaker;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gallerymaker.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class itemDetail extends AppCompatActivity {
    private ListView_ImageList profile_image_lIst = new ListView_ImageList();
    private TextView name;
    private TextView phone_number;
    private ImageView img;
    private EditText memo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detailview);

        Intent intent = getIntent();
        name = (TextView)findViewById(R.id.detail_name);
        phone_number = (TextView)findViewById(R.id.detail_phoneNumber);
        img = (ImageView) findViewById(R.id.detail_img);
        memo = (EditText) findViewById(R.id.detail_memo);


        name.setText( intent.getStringExtra ("name") );
        phone_number.setText( intent.getStringExtra ("phone_number") );

        ListViewAdapter tmp = new ListViewAdapter();
        img.setImageResource( tmp.profile_image_lIst.getImg ( intent.getIntExtra ( "img", 0 ) ) );
        memo.setText( getMemo ( getJson(), intent.getStringExtra ("phone_number") ) );
    }

    public String getJson() {
        String json = "";
        try {
            InputStream is = getResources().getAssets().open("phone_Book.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }

    public String getMemo(String json, String phone_number)
    {
        try{
            JSONArray phoneBook_list = new JSONArray(json);
            for(int i = 0; i < phoneBook_list.length(); i++) {
                JSONObject item = phoneBook_list.getJSONObject(i);
                if ( item.getString("phone_number").equals(phone_number) ) {
                    return item.getString("memo");
                }
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return "empty";
    }
}
