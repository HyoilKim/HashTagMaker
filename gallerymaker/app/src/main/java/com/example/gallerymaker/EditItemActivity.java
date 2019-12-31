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

    private String tmpName;
    private String tmpMemo;
    private String tmpPhoneNumber;
    private int tmpImgIdx;
    private InputMethodManager imm;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok_bar, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        imm.hideSoftInputFromWindow(findViewById(R.id.edit_name).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.edit_phoneNumber).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.edit_memo).getWindowToken(), 0);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        this.name = (EditText) findViewById(R.id.edit_name);
        this.phoneNumber = (EditText) findViewById(R.id.edit_phoneNumber);
        this.img = (ImageView) findViewById(R.id.edit_img);
        this.memo = (EditText) findViewById(R.id.edit_memo);

        final Intent intent = getIntent();
        this.tmpImgIdx = this.imgIdx = intent.getIntExtra("img", 0);
        img.setImageResource( profile_image_lIst.getImg (imgIdx) );

        // 수정 전 정보 저장(tmp...)
        this.tmpName = intent.getStringExtra("name");
        this.tmpPhoneNumber = intent.getStringExtra("phone_number");
        this.tmpMemo = intent.getStringExtra("memo");
        name.setText(tmpName);
        phoneNumber.setText(tmpPhoneNumber);
        memo.setText(tmpMemo);

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

                    for (int i = 0; i < BaseActivity.actList.size(); i++) {
                        BaseActivity.actList.get(i).finish();
                        BaseActivity.actList.remove(i);
                    }
                    Intent intent1 = new Intent(EditItemActivity.this, MainActivity.class);
                    startActivity(intent1);
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

    // edit 버튼 클릭시 화면 전환
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.ok_bar:
                Log.d("complete edit button", "at edit view");
                // item detail에 전송
                // 수정된 정보
                String newPhoneNumber = phoneNumber.getText().toString();
                String newName = name.getText().toString();
                String newMemo = memo.getText().toString();
                int newimg = imgIdx;

                Intent intent = new Intent(EditItemActivity.this, itemDetail.class);
                intent.putExtra("name", newName);
                intent.putExtra("phone_number", newPhoneNumber);
                intent.putExtra("memo", newMemo);
                intent.putExtra("img", imgIdx);
                setResult(EDIT, intent);

                // json 수정
                try {
                    String json = "";
                    String str = "";
                    BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"phoneBook.txt"));
                    while(( ( str = br.readLine() ) != null )) {
                        json += str + "\n";
                    }

                    // array -> list
                    JSONArray jsonArray = new JSONArray(json);
                    ArrayList<JSONObject> jsonList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) jsonList.add((JSONObject) jsonArray.get(i));

                    // 수정 전의 item정보와 json의 정보가 같으면 object수정
                    for (int i = 0; i < jsonList.size(); i++) {
                        JSONObject obj = jsonList.get(i);
                        if( tmpPhoneNumber.equals(obj.getString("phone_number")) &&
                                tmpName.equals(obj.getString("name")) && tmpMemo.equals(obj.getString("memo")) ) {
                            Log.d("same idx", i+"");
                            JSONObject tmpObj = new JSONObject();
                            tmpObj.put("img", newimg);
                            tmpObj.put("name", newName);
                            tmpObj.put("phone_number", newPhoneNumber);
                            tmpObj.put("memo", newMemo);
                            tmpObj.put("isBlock", false);
                            jsonList.set(i, tmpObj);
                            break;
                        }
                    }
                    // 삭제한 내용을 다시 json file에 저장
                    JSONArray updateJsonArray = new JSONArray();
                    for (JSONObject o : jsonList) updateJsonArray.put(o);

                    BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "phoneBook.txt", false));
                    bw.write(updateJsonArray.toString());
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
                finish();
                return true;

            default: return false;
        }
    }
}