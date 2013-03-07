package com.donvigo.GettingThingsDone.Wrappers;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Airline implements Parcelable{
	@SerializedName("Name")
	private String name;
    @SerializedName("FaresFull")
    private ArrayList<AirlineFare> faresFull;
    private long totalAmount;
    private String currency;
    private boolean isSelected;

	public Airline(){
	}

    // constructor that takes a Parcel and gives you an object populated with it's values
    private Airline(Parcel in) {
        name = in.readString();

        if(faresFull == null)
            faresFull = new ArrayList<AirlineFare>();
        in.readList(faresFull, AirlineFare.class.getClassLoader());

        totalAmount = in.readLong();
        currency = in.readString();
        isSelected = (Boolean)in.readValue(Boolean.class.getClassLoader());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AirlineFare> getFaresFull() {
        return faresFull;
    }

    public void setFaresFull(ArrayList<AirlineFare> faresFull) {
        this.faresFull = faresFull;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
        dest.writeList(faresFull);
        dest.writeLong(totalAmount);
        dest.writeString(currency);
        dest.writeValue(isSelected);
	}

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<Airline> CREATOR = new Creator<Airline>() {
        public Airline createFromParcel(Parcel in) {
            return new Airline(in);
        }

        public Airline[] newArray(int size) {
            return new Airline[size];
        }
    };


}
