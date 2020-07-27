package ksnu.dsem.realtimeactivityrecognition;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ksnu.dsem.structure.Acceleration;

public class Accelerometer implements SensorEventListener {

    private Acceleration acc;

    public Accelerometer() {
        this.acc = new Acceleration();
    }

    public Accelerometer(Acceleration acc) {
        this.acc = acc;
    }

    public Acceleration getAcc() {
        return this.acc;
    }

    public void setAcc(Acceleration acc) {
        this.acc = acc;
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acc.setXYZ(event.values[0], event.values[1], event.values[2]);
        }

    }

}