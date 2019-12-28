package com.example.gallerymaker.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.gallerymaker.ListViewAdapter;
import com.example.gallerymaker.ListViewItem;
import com.example.gallerymaker.R;
import com.example.gallerymaker.itemDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HomeFragment extends ListFragment {
    ListViewAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ListViewAdapter() ;
        setListAdapter(adapter);

        // add item at list
//        adapter.addItem(R.drawable.ic_dashboard_black_24dp,
//                "김몰입", "010-7942-7041") ;
//        adapter.addItem(R.drawable.ic_home_black_24dp,
//                "이몰입", "010-6354-1236") ;
//        adapter.addItem(R.drawable.ic_notifications_black_24dp,
//                "정몰입", "010-6830-1232") ;

        Log.d("@@@@@@@@@","1");


        jsonParsing(getJson());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // get TextView's Text.
        ListViewItem item = (ListViewItem) l.getItemAtPosition(position) ;

        String name = item.getName() ;
        String phone_number = item.getPhoneNumber() ;
        int img = item.getImg();
        // image transfer setting
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Intent intent = new Intent(getActivity().getApplicationContext(), itemDetail.class);
        intent.putExtra("name", name);
        intent.putExtra("phone_number", phone_number);
        intent.putExtra("img", img);
        startActivity(intent);

    }

    private String getJson() {
        String json = "";

        Log.d("!!!!!!!!","2");
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

        Log.d("@@@@@@@", "");
        return json;
    }

    private void jsonParsing(String json)
    {
        adapter = new ListViewAdapter() ;
        setListAdapter(adapter);
        Log.d("jsonjsonjosnjson", json);
        try{
            JSONArray phoneBook_list = new JSONArray(json);

            for(int i = 0; i < phoneBook_list.length(); i++) {

                JSONObject item = phoneBook_list.getJSONObject(i);
                Log.d("img", item.getString("img"));
                Log.d("img", item.getString("img"));

//                int img = Integer.parseInt( item.getString("img") );
                String phone_number = item.getString("phone_number");
                String name = item.getString("name");

                Log.d("@@@@@@@@@@@@", name);
                adapter.addItem(0, name, phone_number);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}