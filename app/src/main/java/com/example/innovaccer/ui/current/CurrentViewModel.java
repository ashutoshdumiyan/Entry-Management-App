package com.example.innovaccer.ui.current;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CurrentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is current visitors fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}