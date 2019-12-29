package com.example.gallerymaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gallerymaker.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class EditItemActivity extends AppCompatActivity {
    private ListView_ImageList profile_image_lIst = new ListView_ImageList();
    private EditText name;
    private EditText phoneNumber;
    private EditText memo;
    private ImageView img;
    private JSONArray phoneBook_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        Intent intent = getIntent();
        this.name = (EditText) findViewById(R.id.edit_name);
        this.phoneNumber = (EditText) findViewById(R.id.edit_phoneNumber);
        this.img = (ImageView) findViewById(R.id.edit_img);
        this.memo = (EditText) findViewById(R.id.edit_memo);

        Log.d("eidt name", intent.getStringExtra("name"));
//        memo.setText( getMemo ( getJson(), intent.getStringExtra ("phone_number") ) );

        name.setText( intent.getStringExtra ("name") );
        phoneNumber.setText( intent.getStringExtra ("phone_number") );
        img.setImageResource( profile_image_lIst.getImg ( intent.getIntExtra ( "img", 0 ) ) );
        memo.setText( intent.getStringExtra("memo") );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok_bar, menu);
        return true;
    }

    // edit 버튼 클릭시 화면 전환
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.edit_bar:
                Log.d("block button", "at edit view");
                // item detail에 전송하고
                // json 수정
                finish();
                return true;
            case R.id.edit_delete:
                Log.d("delete button","at edit view");

            default: return false;
        }
    }


}