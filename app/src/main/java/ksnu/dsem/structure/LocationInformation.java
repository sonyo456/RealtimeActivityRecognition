package ksnu.dsem.structure;

public class LocationInformation {
    private double latitude;
    private double longitude;
    private double speed;

    public LocationInformation() {
        this(0,0,0);
    }

    public LocationInformation(double lat, double lon, double spd) {
        this.latitude = lat;
        this.longitude = lon;
        this.speed = spd;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    public void getSpeed(double spd) {
        this.speed = spd;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setLocationInformation(double lat, double lon, double spd) {
        this.latitude = lat;
        this.longitude = lon;
        this.speed = spd;
    }
}
