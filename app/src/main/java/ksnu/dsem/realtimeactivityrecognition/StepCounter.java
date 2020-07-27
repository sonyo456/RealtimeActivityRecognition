package ksnu.dsem.realtimeactivityrecognition;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import ksnu.dsem.structure.StepCount;

public class StepCounter implements SensorEventListener {

    private StepCount tcount;

    public StepCounter() {
        this.tcount = new StepCount();
    }

    public StepCounter(StepCount tcount){
        this.tcount = tcount;
    }

    public StepCount getStep() {
        return tcount;
    }

    public void setStep(StepCount tcount) {
        this.tcount = tcount;
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType()==Sensor.TYPE_STEP_COUNTER){
            if(tcount.getCumulativeStep()<1){
                tcount.setInitialStep((int)event.values[0]);
            }
            tcount.setCumulativeStep((int)event.values[0]);
        }

    }

}