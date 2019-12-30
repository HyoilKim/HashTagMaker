package com.example.gallerymaker;

import java.util.ArrayList;

public class ListView_ImageList {
    private ArrayList<Integer> profile_image_lIst = new ArrayList<>();

    ListView_ImageList() {
        profile_image_lIst.add(R.drawable.ic_dashboard_black_24dp);
        profile_image_lIst.add(R.drawable.ic_notifications_black_24dp);
        profile_image_lIst.add(R.drawable.ic_home_black_24dp);
    }

    public int getImg(int i) { return this.profile_image_lIst.get(i); }
    public void addImg(int img) { profile_image_lIst.add(img); }
    public void setImg(int i, int img) { profile_image_lIst.set(i, img); }
}
