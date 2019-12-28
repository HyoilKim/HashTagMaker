package com.example.gallerymaker;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ListViewItem {
    private String name ;
    private String phone_number ;
    private int img;

    public void setImg(int img) { this.img = img; }
    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phone_number) { this.phone_number = phone_number; }

    public int getImg() { return this.img; }
    public String getName() {
        return this.name;
    }
    public String getPhoneNumber() {
        return this.phone_number;
    }
}