package com.example.yuunagi.ui.dashboard;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<Integer> fansNumber;

    public DashboardViewModel() {
        fansNumber = new MutableLiveData<>();
        fansNumber.setValue(0);
    }

    public LiveData<Integer> getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(Integer fansNumber) {
        Log.d("set", fansNumber.toString());
        this.fansNumber.setValue(fansNumber);
    }
}