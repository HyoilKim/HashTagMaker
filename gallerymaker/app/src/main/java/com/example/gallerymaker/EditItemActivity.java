package com.example.gallerymaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {
    static final int EDIT = 1;
    private ListView_ImageList profile_image_lIst = new ListView_ImageList();
    private EditText name;
    private EditText phoneNumber;
    private EditText memo;
    private ImageView img;
    private int imgIdx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        final Intent intent = getIntent();
        this.name = (EditText) findViewById(R.id.edit_name);
        this.phoneNumber = (EditText) findViewById(R.id.edit_phoneNumber);
        this.img = (ImageView) findViewById(R.id.edit_img);
        this.memo = (EditText) findViewById(R.id.edit_memo);

        this.imgIdx = intent.getIntExtra("img", 0);
        img.setImageResource( profile_image_lIst.getImg (imgIdx) );
        name.setText( intent.getStringExtra ("name") );
        phoneNumber.setText( intent.getStringExtra ("phone_number") );
        memo.setText( intent.getStringExtra("memo") );

        // 클릭시 json에 있는 item정보 삭제
        Button deleteItemButton = (Button) findViewById(R.id.edit_delete);
        deleteItemButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("delete","button clicked");
                String json = "";
                String str = "";
                try {
                    BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"phoneBook.txt"));
                    while(( ( str = br.readLine() ) != null )) {
                        json += str + "\n";
                    }

                    // array -> list
                    JSONArray jsonArray = new JSONArray(json);
                    ArrayList<JSONObject> jsonList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) jsonList.add((JSONObject) jsonArray.get(i));

                    // item의 전화번호와 json의 전화번호가 같으면 해당 object삭제
                    String itemPhoneNumber = phoneNumber.getText().toString();
                    String tmp = "";
                    for (int i = 0; i < jsonList.size(); i++) {
                        if ( jsonList.get(i).has("phone_number") ) {
                            tmp = jsonList.get(i).getString("phone_number");
                            if( tmp.equals(itemPhoneNumber) ) {
                                jsonList.remove(i);
                                break;
                            }
                        } else {
                            jsonList.remove(i);
                            break;
                        }
                    }
                    // 삭제한 내용을 다시 json file에 저장
                    JSONArray deletedJsonArray = new JSONArray();
                    for (JSONObject o : jsonList) deletedJsonArray.put(o);

                    BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "phoneBook.txt", false));
                    bw.write(deletedJsonArray.toString());
                    bw.close();

                    //activity 2개 닫기
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
            case R.id.ok_bar:
                Log.d("ok button", "at edit view");
                // item detail에 전송
                Intent intent = new Intent();

                intent.putExtra("name", this.name.getText().toString());
                intent.putExtra("phone_number", this.phoneNumber.getText().toString());
                intent.putExtra("img", this.imgIdx);
                intent.putExtra("memo", this.memo.getText().toString());
                setResult(EDIT, intent);                                               // 필수(이유 모름)

                // json 수정
                finish();
                return true;

            default: return false;
        }
    }



}