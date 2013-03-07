package com.donvigo.GettingThingsDone.Services.ServiceUtils;

import com.donvigo.GettingThingsDone.Wrappers.Airline;
import com.donvigo.GettingThingsDone.Wrappers.CityInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetFaresResponse {
    @SerializedName("Airlines")
    private ArrayList<Airline> airlines;

    public ArrayList<Airline> getAirlines() {
        return airlines;
    }

    public void setAirlines(ArrayList<Airline> airlines) {
        this.airlines = airlines;
    }
}
