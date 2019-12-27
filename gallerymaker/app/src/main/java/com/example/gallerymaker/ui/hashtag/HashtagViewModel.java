package com.example.gallerymaker.ui.hashtag;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HashtagViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HashtagViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is hashtag fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}