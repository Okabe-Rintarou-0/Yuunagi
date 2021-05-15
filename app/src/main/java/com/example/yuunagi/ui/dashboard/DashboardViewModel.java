package com.example.yuunagi.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> fansNumber;

    public DashboardViewModel() {
        fansNumber = new MutableLiveData<>();
        fansNumber.setValue("0");
    }

    public LiveData<String> getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(String fansNumber) {
        Log.d("set", fansNumber);
        this.fansNumber.setValue(fansNumber);
    }
}