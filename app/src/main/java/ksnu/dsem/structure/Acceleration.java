package ksnu.dsem.structure;

public class Acceleration {
    private double accx;
    private double accy;
    private double accz;
    private double liAccx;
    private double liAccy;
    private double liAccz;

    public static double svm;

    //    private double msvm;
    public Acceleration() {

        this(0, 0, 0, 0, 0, 0, 0);
    }

    public Acceleration(double accx, double accy, double accz, double liAccx, double liAccy,double liAccz,double svm) {
        this.accx = accx;
        this.accy = accy;
        this.accz = accz;
        this.liAccx = liAccx;
        this.liAccy = liAccy;
        this.liAccz = liAccz;
        this.svm = svm;

    }

    public void setAccx(double accx) {
        this.accx = accx;
    }

    public void setAccy(double accy) {
        this.accy = accy;
    }

    public void setAccz(double accz) {
        this.accz = accz;
    }
    public void setLiAccx(double liAccx){
        this.liAccx = liAccx;
    }

    public void setLiAccy(double liAccy) {
        this.liAccy = liAccy;
    }

    public void setLiAccz(double liAccz) {
        this.liAccz = liAccz;
    }

    public void setSvm(double svm) {
        this.svm = svm;
    }

    public void calculateSVM () {
        this.svm = Math.sqrt(Math.pow(this.accx, 2) + Math.pow(this.accy, 2) + Math.pow(this.accz, 2));
    }

    public double getAccx() {
        return this.accx;
    }

    public double getAccy() {
        return this.accy;
    }

    public double getAccz() {
        return this.accz;
    }

    public double getLiAccx() {
        return liAccx;
    }

    public double getLiAccy() {
        return liAccy;
    }

    public double getLiAccz() {
        return liAccz;
    }

    public double getSvm() {
        return this.svm;
    }

    public void setXYZ(double x, double y, double z) {
        this.accx = x;
        this.accy = y;
        this.accz = z;
        this.calculateSVM();
    }
    public void setLinearXYZ(double liearx, double lieary, double liearz) {
        this.liAccx = liearx;
        this.liAccy = lieary;
        this.liAccz = liearz;
    }

    public void setAccelerometer(double x, double y, double z, double svm) {
        this.accx = x;
        this.accy = y;
        this.accz = z;
        this.svm = svm;
    }
}
