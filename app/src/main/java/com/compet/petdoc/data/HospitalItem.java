package com.compet.petdoc.data;

/**
 * Created by Mu on 2016-11-30.
 */

public class HospitalItem implements java.io.Serializable {

    private String hosName;

    private String address;

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
}
