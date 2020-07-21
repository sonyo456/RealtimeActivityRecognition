package ksnu.dsem.structure;

public class Acceleration {
    private double x;
    private double y;
    private double z;
    public static double svm;

    //    private double msvm;
    public Acceleration() {

        this(0, 0, 0, 0);
    }

    public Acceleration(double x, double y, double z, double svm) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.svm = svm;

    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setSvm(double svm) {
        this.svm = svm;
    }

    public void calculateSVM () {
        this.svm = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getSvm() {
        return this.svm;
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setAccelerometer(double x, double y, double z, double svm) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.svm = svm;
    }
}
