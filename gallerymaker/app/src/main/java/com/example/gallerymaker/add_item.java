package com.example.gallerymaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class add_item extends AppCompatActivity {
    private String name;
    private String phoneNumber;
    private String memo;
    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        ImageButton imgButton = (ImageButton) findViewById(R.id.add_img);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // list image에 넣고 참조 할 수 있게 or url
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("widnow","clicked");
        imm.hideSoftInputFromWindow(findViewById(R.id.add_name).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.add_phoneNumber).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.add_memo).getWindowToken(), 0);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.ok_bar:
                Log.d("complete_add_bar", "clicked");

                // 추가 할 연락처 정보 listView에 넘기기
                EditText nameView = (EditText)findViewById(R.id.add_name);
                EditText phoneNumberView = (EditText)findViewById(R.id.add_phoneNumber);
                EditText memoView = (EditText)findViewById(R.id.add_memo);

                this.name = nameView.getText().toString();
                this.phoneNumber = phoneNumberView.getText().toString();
                this.memo = memoView.getText().toString();

                //json에 집어넣기
                try {
                    addJsonObject(0, name, phoneNumber, false, memo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(add_item.this, MainActivity.class);
                setResult(HomeFragment.ADD_ITEM, intent);
                finish();

                return true;
            default: return false;
        }
    }

    private void addJsonObject(int img, String name, String phoneNumber, boolean isBlock, String memo) throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray jsonArray;
        try {
            // 기존 json 읽어서 배열 만들기
            String json = "";
            String str = "";
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"phoneBook.txt"));
            while(( ( str = br.readLine() ) != null )) {
                json += str + "\n";
            }
            jsonArray = new JSONArray(json);

            // 입력 값을 jsonobject로 만들고 json 배열에 넣기
            obj.put("img", img);
            obj.put("name", name);
            obj.put("phone_number", phoneNumber);
            obj.put("isBlock", isBlock);
            obj.put("memo", memo);
            jsonArray.put(obj);

            // 정렬
            sortJsonArray(jsonArray);

            // 수정된 josn 배열을 josn파일에 넣기
            BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "phoneBook.txt", false));
            bw.write(jsonArray.toString());
            bw.close();

            br.close();
//            Log.d("resresres", jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.d("adpater","changed");
//        HomeFragment.adapter.notifyDataSetChanged();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                HomeFragment.adapter.notifyDataSetChanged();
//            }
//        });

    }

    public void sortJsonArray(JSONArray jsonArray) {
        List<JSONObject> jsonList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonList.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(jsonList, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                String str1 = "";
                String str2 = "";
                try {
                    str1 = o1.getString("name");
                    str2 = o2.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return str1.compareTo(str2);
            }
        });

        for (int i = 0; i < jsonList.size(); i++) {
            try {
                jsonArray.put(i, jsonList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
