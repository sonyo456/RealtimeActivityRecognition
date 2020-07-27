package ksnu.dsem.structure;

public class StepCount {
    private int cumulativeStep; // cumulativeStep
    private int initialStep;    // initialStep
    private int step;    // step

    //    private double msvm;
    public StepCount() {

        this(0, 0, 0);
    }

    public StepCount(int cumulativeStep, int initialStep, int step) {
        this.cumulativeStep = cumulativeStep;
        this.initialStep = initialStep;
        this.step = step;

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


    public double getCumulativeStep() {
        return this.cumulativeStep;
    }

    public double getInitialStep() {
        return this.initialStep;
    }

    public double getStep() {
        return this.step;
    }

}

