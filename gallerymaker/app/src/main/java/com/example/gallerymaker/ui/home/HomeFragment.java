package com.example.gallerymaker.ui.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

public class HomeFragment extends ListFragment {
    ListViewAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ListViewAdapter() ;
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        ListView listView = (ListView) view.findViewById(R.id.listview1);
        setListAdapter(adapter);

        // add item at list
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dashboard_black_24dp),
                "김몰입", "010-7942-7041") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_home_black_24dp),
                "이몰입", "010-6354-1236") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_notifications_black_24dp),
                "정몰입", "010-6830-1232") ;

        // ************* extends Fragment ************* //
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//                // get item
//                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;
//
//                String name = item.getName() ;
//                String phone_number = item.getPhoneNumber() ;
//                Drawable iconDrawable = item.getIcon() ;
//
//                // TODO : use item data.
//            }
//        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // get TextView's Text.
        ListViewItem item = (ListViewItem) l.getItemAtPosition(position) ;

        String name = item.getName() ;
        String phone_number = item.getPhoneNumber() ;
        Drawable iconDrawable = item.getIcon() ;
        Log.d("name", name);

        Intent intent = new Intent(getActivity().getApplicationContext(), itemDetail.class);
        intent.putExtra("name", name);
        intent.putExtra("phone_number", phone_number);
//        intent.putExtra("icon", iconDrawable);
        startActivity(intent);


        // TODO : use item data.
    }
}