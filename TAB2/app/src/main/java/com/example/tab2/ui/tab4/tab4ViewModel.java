package com.example.tab2.ui.tab4;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class tab4ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public tab4ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tab4 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}