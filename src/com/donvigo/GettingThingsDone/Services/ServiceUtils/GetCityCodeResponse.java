package com.donvigo.GettingThingsDone.Services.ServiceUtils;

import com.donvigo.GettingThingsDone.Wrappers.CityInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetCityCodeResponse {
    @SerializedName("Count")
    private int count;
    @SerializedName("Array")
    private ArrayList<CityInfo> cityInfo;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<CityInfo> getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(ArrayList<CityInfo> cityInfo) {
        this.cityInfo = cityInfo;
    }
}
