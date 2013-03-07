package com.donvigo.GettingThingsDone.Services.ServiceUtils;

import com.google.gson.annotations.SerializedName;

public class RequestStateResponse {
    @SerializedName("Completed")
    private String completed;
    @SerializedName("Error")
    private String error;

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
