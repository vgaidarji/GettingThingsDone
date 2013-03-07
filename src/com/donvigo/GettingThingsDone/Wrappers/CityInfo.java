package com.donvigo.GettingThingsDone.Wrappers;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class CityInfo implements Parcelable{
    @SerializedName("Code")
    private String code;
	@SerializedName("City")
	private String city;
    @SerializedName("Country")
    private String country;
    @SerializedName("Airport")
    private String airport;
    @SerializedName("Data")
    private String data;
    @SerializedName("CityCode")
    private String cityCode;
    private transient String ID;

	public CityInfo(){
	}

    // constructor that takes a Parcel and gives you an object populated with it's values
    private CityInfo(Parcel in) {
        code = in.readString();
        city = in.readString();
        country = in.readString();
        airport = in.readString();
        data = in.readString();
        cityCode = in.readString();
        ID = in.readString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(airport);
        dest.writeString(data);
        dest.writeString(cityCode);
        dest.writeString(ID);
	}

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<CityInfo> CREATOR = new Creator<CityInfo>() {
        public CityInfo createFromParcel(Parcel in) {
            return new CityInfo(in);
        }

        public CityInfo[] newArray(int size) {
            return new CityInfo[size];
        }
    };


}
