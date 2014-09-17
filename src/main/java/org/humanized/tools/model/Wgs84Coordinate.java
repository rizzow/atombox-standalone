package org.humanized.tools.model;

public class Wgs84Coordinate {
    protected Double mLatitude;
    protected Double mLongitude;


    public Wgs84Coordinate() {
    }


    public Wgs84Coordinate(Double latitude, Double longitude) {
        set(latitude, longitude);
    }


    public Double getLatitude() {
        return mLatitude;
    }


    public Double getLongitude() {
        return mLongitude;
    }


    public void set(Double latitude, Double longitude) {

        if (latitude != null) {
            assert latitude <= 90.0 && latitude >= -90.0;
            this.mLatitude = latitude;
        }

        this.mLongitude = longitude;
    }


    public void setLatitude(Double latitude) {
        set(latitude, this.mLongitude);
    }


    public void setLongitude(Double longitude) {
        set(this.mLatitude, longitude);
    }
}
