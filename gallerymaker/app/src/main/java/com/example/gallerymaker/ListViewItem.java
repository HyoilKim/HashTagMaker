package com.example.gallerymaker;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ListViewItem {
    private String name ;
    private String phone_number ;
    private ImageView imageView;
    private int img;
    private boolean isBlock;
    private String memo;

    public void setImg(int img) { this.img = img; }
    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phone_number) { this.phone_number = phone_number; }
    public void setMemo(String memo) { this.memo = memo; }
    public void setIsBlock(boolean isBlock) { this.isBlock = isBlock; }
//    public void setImageView(ImageView imageView) { this.imageView = imageView; }

    public int getImg() { return this.img; }
    public String getName() {
        return this.name;
    }
    public String getPhoneNumber() {
        return this.phone_number;
    }
    public String getMemo() { return this.memo; }
    public boolean getIsBlock() { return this.isBlock; }
//    public ImageView getImageView() { return this.imageView; }
}