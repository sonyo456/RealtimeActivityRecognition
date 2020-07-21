package ksnu.dsem.structure;

public class StepCount {
    private int step;
    private int newstep;
    private int calstep;

    //    private double msvm;
    public StepCount() {

        this(0, 0, 0);
    }

    public StepCount(int step, int newstep, int calstep) {
        this.step = step;
        this.newstep = newstep;
        this.calstep = calstep;

    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setNewstep(int newstep) {
        this.newstep = newstep;
    }

    public void setCalstep(int calstep) {
        this.calstep = calstep;
    }

    public void calculateStep () {
        this.calstep = newstep - step;
    }

    public double getStep() {
        return this.step;
    }

    public double getNewstep() {
        return this.newstep;
    }

    public double getCalstep() {
        return this.calstep;
    }

}

