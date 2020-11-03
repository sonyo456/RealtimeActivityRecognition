package ksnu.dsem.realtimeactivityrecognition;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ksnu.dsem.structure.Acceleration;
import ksnu.dsem.structure.LocationInformation;
import ksnu.dsem.structure.StepCount;
import weka.classifiers.Classifier;
import weka.core.Attribute;

import weka.core.DenseInstance;
import weka.core.Instances;

public class RFModel {
    //private final Attribute attribute_HR = new Attribute("HR");
    private final Attribute attribute_LAT = new Attribute("LAT");
    private final Attribute attribute_LON = new Attribute("LON");
    private final Attribute attribute_SVM = new Attribute("SVM");
    private final Attribute attribute_SPEED = new Attribute("SPEED");
    private final Attribute attribute_STEP_DEV = new Attribute("STEP_DEV");
    private final List<String> actClassList = new ArrayList<String>() {
        {
            add("WALKING");
            add("RUNNING");
            add("STATIONARY");
            add("IN_VEHICLE");
            add("IN_CAR");
        }
    };

    private ArrayList<Attribute> attributeList;
    private Instances dataUnpredicted;
    private Classifier classifier;


    //    public String result2;
    public LocationInformation location;

    public Acceleration acc;

    public StepCount scount;

    public RFModel() {
        this.attributeList = new ArrayList<Attribute>(2) {
            {
                //add(attribute_HR);
                add(attribute_LAT);
                add(attribute_LON);
                add(attribute_SVM);
                add(attribute_SPEED);
                add(attribute_STEP_DEV);
                Attribute attributeClass = new Attribute("@@class@@", actClassList);
                add(attributeClass);
            }
        };

        this.dataUnpredicted = new Instances("CurrentInstance",
                attributeList, 1);

        int attnum = this.dataUnpredicted.numAttributes();
        this.dataUnpredicted.setClassIndex(attnum - 1);
        Log.d("actrecog", attnum + "");

        this.location = new LocationInformation();
        this.acc = new Acceleration();
        this.scount = new StepCount();
        //AssetManager assetManager = getAssets();
        try {
            //cls = (Classifier) weka.core.SerializationHelper.read(getAssets().open("REAL_MODEL.model"));
            //실제 핸드폰
            classifier = (Classifier) weka.core.SerializationHelper.read("/data/data/ksnu.dsem.realtimeactivityrecognition/MOBILE_MODEL.model");

//            classifier = (Classifier) weka.core.SerializationHelper.read("/data/user/0/ksnu.dsem.realtimeactivityrecognition/model/MOBILE_MODEL.model");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (classifier == null) {
            Log.d("actrecog", "Classifier is Null");
        }
    }

    public void setLocation(double latitude, double longitude, double speed) {
        this.location.setLocationInformation(latitude, longitude, speed);
    }

    public void setLocation(LocationInformation locationinformation) {
        this.location = locationinformation;
    }

    public void setSvm(double x, double y, double z) {
        this.acc.setXYZ(x, y, z);
        this.acc.calculateSVM();
    }
    public void setStep_dev(int step_dev) {
        this.scount.setStep_dev(step_dev);
    }
    public void setSvm(Acceleration acc) {
        this.acc = acc;
    }

    //매개변수
    public String classifyActtype(double latitude, double longitude, double speed, double svm, int step_dev) {
        String acttype = "";

        DenseInstance currentInstance = new DenseInstance(dataUnpredicted.numAttributes()) {
            {

                setValue(attribute_LAT, latitude);
                setValue(attribute_LON, longitude);
                setValue(attribute_SPEED, speed);
                setValue(attribute_SVM, svm);
                setValue(attribute_STEP_DEV, step_dev);

            }
        };
        currentInstance.setDataset(dataUnpredicted);

        // 모델에 예측할 instance

        try {
            double result = classifier.classifyInstance(currentInstance);
//            acttype = actClassList.get(new Double(result).intValue());
            acttype = actClassList.get(Double.valueOf(result).intValue());
            Log.d("actrecog", "result behavior: " + acttype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acttype;
    }
}
