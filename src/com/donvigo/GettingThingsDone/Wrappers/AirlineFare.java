package com.donvigo.GettingThingsDone.Wrappers;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AirlineFare implements Parcelable{
	@SerializedName("TotalAmount")
	private String totalAmount;
    @SerializedName("Currency")
    private String currency;

	public AirlineFare(){
	}

    // constructor that takes a Parcel and gives you an object populated with it's values
    private AirlineFare(Parcel in) {
        totalAmount = in.readString();
        currency = in.readString();
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(totalAmount);
        dest.writeString(currency);
	}

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<AirlineFare> CREATOR = new Creator<AirlineFare>() {
        public AirlineFare createFromParcel(Parcel in) {
            return new AirlineFare(in);
        }

        public AirlineFare[] newArray(int size) {
            return new AirlineFare[size];
        }
    };


}
