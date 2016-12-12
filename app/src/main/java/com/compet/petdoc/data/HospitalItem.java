package com.compet.petdoc.data;

import android.provider.BaseColumns;

/**
 * Created by Mu on 2016-11-30.
 */

public class HospitalItem implements java.io.Serializable {

    private String hosName;

    private String address;

    private String phoneNumber;

    private double latitude;

    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public interface HospitalInfo extends BaseColumns {

        public static final String TABLE = "hospitalInfo";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_LOCATION = "location";

        public static final String COLUMN_PHONE_NUMBER = "phoneNumber";



    }
}
