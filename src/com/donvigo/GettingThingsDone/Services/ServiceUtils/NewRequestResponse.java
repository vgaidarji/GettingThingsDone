package com.donvigo.GettingThingsDone.Services.ServiceUtils;

import com.google.gson.annotations.SerializedName;

public class NewRequestResponse {
    @SerializedName("Id")
    private String ID;
    @SerializedName("Error")
    private String error;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
