package com.example.carrental.UserJcode.viewmyprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class U_ViewUserProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public U_ViewUserProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}