package com.example.gallerymaker.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.example.gallerymaker.ListViewAdapter;
import com.example.gallerymaker.ListViewItem;
import com.example.gallerymaker.MainActivity;
import com.example.gallerymaker.R;
import com.example.gallerymaker.add_item;
import com.example.gallerymaker.itemDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class HomeFragment extends ListFragment {
    private String memo;
    private boolean isBlock;
    private ListViewAdapter adapter;
    public static final int ADD_ITEM = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // show listView from json
        jsonParsing(getJson());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // bar 추가
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_bar, menu);
    }

    // 전화번호 아이템 추가 버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment add_fragment = new Fragment();
        switch ( item.getItemId() ) {
            case R.id.add_bar:
                Log.d("add_bar", "clicked");
                Intent intent = new Intent(getActivity(), add_item.class);
                getActivity().startActivityForResult(intent, ADD_ITEM);
                return true;
            default: return false;
        }
    }

    // 아이템 추가 화면에서 ok를 눌렀을 경우
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM) {
            // json 다시 읽어오기
            jsonParsing(getJson());
            adapter.notifyDataSetChanged();

        }
    }

    // 아이템 클릭 시 디테일 뷰로 전환
    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // get TextView's Text.
        ListViewItem item = (ListViewItem) l.getItemAtPosition(position) ;

        String name = item.getName() ;
        String phone_number = item.getPhoneNumber() ;
        String memo = item.getMemo();
        int img = item.getImg();
        // image transfer setting
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Intent intent = new Intent(getActivity().getApplicationContext(), itemDetail.class);
        intent.putExtra("name", name);
        intent.putExtra("phone_number", phone_number);
        intent.putExtra("img", img);
        intent.putExtra("memo", memo);
        startActivityForResult(intent, 1);

    }

    // asset폴더의 json파일을 읽어 string 타입으로 return
    public String getJson() {
        String json = "";
        int data = -1;
        try {
              // read assets file
//            InputStream is = getResources().getAssets().open("phone_Book.txt");
//            int fileSize = is.available();
//
//            byte[] buffer = new byte[fileSize];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//            Log.d("asset json", json);
//
//            BufferedWriter bw = new BufferedWriter(new FileWriter(getActivity().getFilesDir() + "phoneBook.txt", false));
//            bw.write(json);
//            bw.close();
//
            BufferedReader br = new BufferedReader(new FileReader(getActivity().getFilesDir()+"phoneBook.txt"));
            String str = null;
            while(( ( str = br.readLine() ) != null )) {
                json += str +"\n";
            }

            Log.d("text file json", json);
            br.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }

    // json파일로 listView의 item 각각 추가
    private void jsonParsing(String json) {
        adapter = new ListViewAdapter() ;
        setListAdapter(adapter);
        try{
            JSONArray phoneBook_list = new JSONArray(json);

            for(int i = 0; i < phoneBook_list.length(); i++) {
                JSONObject item = phoneBook_list.getJSONObject(i);

                int img = Integer.parseInt(item.getString("img"));
                String phone_number = item.getString("phone_number");
                String name = item.getString("name");
                String memo = item.getString("memo");
                isBlock = Boolean.valueOf(item.getString("isBlock"));
                adapter.addItem(img, name, phone_number, isBlock, memo);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}