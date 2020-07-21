package ksnu.dsem.realtimeactivityrecognition;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ksnu.dsem.structure.Acceleration;

public class Accelerometer  implements SensorEventListener {

    private Acceleration acc;

    public Accelerometer() {
        this.acc = new Acceleration();
    }

    public Accelerometer(Acceleration acc){
        this.acc = acc;
    }

    public Acceleration getAcc() {
        return acc;
    }

    public void setAcc(Acceleration acc) {
        this.acc = acc;
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            acc.setX(event.values[0]);
            acc.setY(event.values[1]);
            acc.setZ(event.values[2]);
            acc.calculateSVM();
//            acc.setSvm(event.values[0], event.values[1], event.values[2]);
//            x = event.values[0];
//            y = event.values[1];
//            z = event.values[2];
//            acc = new Acceleration(x, y, z);
//            model.setSvm(acc.getX(),acc.getY(), acc.getZ());

        }

    }

}