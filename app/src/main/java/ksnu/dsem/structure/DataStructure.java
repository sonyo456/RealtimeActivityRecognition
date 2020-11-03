package ksnu.dsem.structure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataStructure {
    private String currtime;
    private double lat;
    private double lon;
    private double x;
    private double y;
    private double z;
    private double speed;
    private double svm;
    private int step;
    private int stepbefore;
    private int step_dev;
    private String curracttype;
    private String corracttype;
    private String date;
    ArrayList<String> AccArray;

    public DataStructure() {
        this("", 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "");
    }

    public DataStructure(String currtime, double lat, double lon, double x, double y, double z, double speed, double svm, int step, int step_dev, String curracttype, String corracttype) {
        this.currtime = currtime;
        this.lat = lat;
        this.lon = lon;
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
        this.svm = svm;
        this.step = step;
        this.step_dev = step_dev;
        this.curracttype = curracttype;
        this.corracttype = corracttype;
    }

    public void setCurrentData(double lat, double lon, double speed, double svm, int step, String curracttype) {
        this.updateCurrentTime();
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.svm = svm;
        this.step_dev = step - this.step;
        this.step = step;
        this.curracttype = curracttype;
    }

    public void setCorrectData(double lat, double lon, double x, double y, double z, double speed, double svm, int step, String corracttype) {
        this.updateCurrentTime();
        this.lat = lat;
        this.lon = lon;
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
        this.svm = svm;
        this.step = step;
        this.corracttype = corracttype;
    }

    public void setXYZtData(ArrayList<String> AccArray) {
        this.updateCurrentTime();
        this.AccArray = AccArray;
    }

    public void setServerData(double lat, double lon, double x, double y, double z, double speed, double svm, int step){
        this.updateCurrentTime();
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.svm = svm;
        this.step = step;
        this.step_dev = step - this.step;
    }
    public void updateCurrentTime() {
        long now = System.currentTimeMillis();
        Date dt = new Date(now);
        this.currtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
        this.date = new SimpleDateFormat("yyyyMMdd").format(dt);
    }

    public String getCurrtime() {

        return currtime;
    }

    public void setCurrtime(String currtime) {
        this.currtime = currtime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setStep_dev(int step_dev) {
        this.step_dev = step_dev;
    }

    public int getStep_dev() {
        return step_dev;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSvm() {
        return svm;
    }

    public void setSvm(double svm) {
        this.svm = svm;
    }

    public String getCurracttype() {
        return curracttype;
    }

    public void setCurracttype(String curracttype) {
        this.curracttype = curracttype;
    }

    public String getCorracttype() {
        return corracttype;
    }

    public void setCorracttype(String corracttype) {
        this.corracttype = corracttype;
    }

    public String getContent() {
        String content = currtime + "," + lat + "," + lon + "," + x + "," + y + "," + z + "," + speed + "," + svm + "," + step + "," + curracttype;

        return content;
    }

    public String getCorrdata() {
        String data = currtime + "," + lat + "," + lon + "," + x + "," + y + "," + z + "," + speed + "," + svm + "," + step + "," + corracttype;

        return data;
    }

    public String getXYZdata() {
        String data = currtime + "," + AccArray;

        return data;
    }

    public String getSendServer() {
        String data = lat + "," + lon + "," + x + "," + y + "," + z + "," + speed + "," + svm + "," + step + "," + step_dev + "," + curracttype;

        return data;
    }

}