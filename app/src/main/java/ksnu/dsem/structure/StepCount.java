package ksnu.dsem.structure;

public class StepCount {
    private int cumulativeStep; // cumulativeStep
    private int initialStep;    // initialStep
    private int step;    // step
    private int step_dev;

    //    private double msvm;
    public StepCount() {

        this(0, 0, 0, 0);
    }

    public StepCount(int cumulativeStep, int initialStep, int step, int step_dev) {
        this.cumulativeStep = cumulativeStep;
        this.initialStep = initialStep;
        this.step = step;
        this.step_dev = step_dev;

    }

    public void setCumulativeStep(int cumulativeStep) {
        this.cumulativeStep = cumulativeStep;
        this.step = cumulativeStep-this.initialStep;

    }

    public void setInitialStep(int initialStep) {
        this.initialStep = initialStep;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setStep_dev(int step_dev) {
        this.step_dev = step_dev;
    }



    public double getCumulativeStep() {
        return this.cumulativeStep;
    }

    public double getInitialStep() {
        return this.initialStep;
    }

    public double getStep() {
        return this.step;
    }

    public int getStep_dev() {
        return this.step_dev;
    }
}

