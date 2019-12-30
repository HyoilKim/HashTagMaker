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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends ListFragment {
    private String memo;
    private boolean isBlock;
    private Bitmap bitmap;

    private Integer[] profile_image_lIst = {
            R.drawable.ic_dashboard_black_24dp,
            R.drawable.ic_notifications_black_24dp,
            R.drawable.ic_home_black_24dp
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // show listView from json
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

    private void jsonParsing(String json) {
        ListViewAdapter adapter = new ListViewAdapter() ;
        setListAdapter(adapter);
        Log.d("jsonjsonjosnjson", json);
        try{
            JSONArray phoneBook_list = new JSONArray(json);

            for(int i = 0; i < phoneBook_list.length(); i++) {

                JSONObject item = phoneBook_list.getJSONObject(i);

                int img = Integer.parseInt(item.getString("img"));

                String phone_number = item.getString("phone_number");
                String name = item.getString("name");
                isBlock = Boolean.valueOf(item.getString("isBlock"));

                adapter.addItem(img, name, phone_number, isBlock, memo);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


// http통신을 하여 url의 image불러오기(http통신)
//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//
//                            Log.d("url","connect");
//                            URL url = new URL("https://i.ibb.co/gRKWCXD/image.png");
//                            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//                            conn.setDoInput(true);
//                            conn.connect();
//
//                            InputStream is = conn.getInputStream();
//                            bitmap = BitmapFactory.decodeStream(is);
//
//                        } catch (MalformedURLException e) {
//                            Log.d("url","error");
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            Log.d("url","error");
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                thread.start();
//                Log.d("url","connect");

//                try {
//                    Log.d("url","connect");
//                    thread.join();
//                    imageView.setImageBitmap(bitmap);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
