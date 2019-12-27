package com.example.gallerymaker;
import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String name ;
    private String phone_number ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String name) {
        this.name = name ;
    }
    public void setDesc(String phone_number) { this.phone_number = phone_number; }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getName() {
        return this.name;
    }
    public String getPhoneNumber() {
        return this.phone_number;
    }
}