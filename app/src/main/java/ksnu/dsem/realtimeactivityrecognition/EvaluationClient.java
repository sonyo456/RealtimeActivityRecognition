package ksnu.dsem.realtimeactivityrecognition;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import ksnu.dsem.structure.DataStructure;

public class EvaluationClient {
    String serverIP = "203.234.62.144";
    private int port = 15020;
    private Socket m_Socket;
    private String currtime;
    private double latitude;
    private double longitude;
    private double x;
    private double y;
    private double z;
    private int stepCount;
    private int step_dev;
    private double speed;
    private double svm;
    private String acttype;
    private String userDataStr;
    PrintWriter sendWriter;
    BufferedReader br;
    public DataStructure ds;
    private Socket c_socket;

    public EvaluationClient(DataStructure ds){
        this.acttype = acttype;
        this.ds = ds;
    }
//    public EvaluationClient(double latitude, double longitude, double x, double y, double z, double svm, double speed, int stepCount, int step_dev) {
//        updateCurrentTime();
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        this.svm = svm;
//        this.speed = speed;
//        this.stepCount = stepCount;
//        this.step_dev = step_dev;
//    }
    public void updateCurrentTime() {
        long now = System.currentTimeMillis();
        Date dt = new Date(now);
        this.currtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
    }

    public void setUserData(double latitude, double longitude, double x, double y, double z, double svm, double speed, int stepCount, int step_dev) {
        updateCurrentTime();
        userDataStr = currtime + "," + latitude + "," + longitude + "," + x + "," + y + "," + z + "," + svm + "," + speed + "," + stepCount + "," + step_dev;
    }
    public void setActtype(String acttype) {
        this.acttype = acttype;
    }
    public String getActtype() {
        return acttype;
    }

    public String evaluation_Client(double latitude, double longitude, double x, double y, double z, double svm, double speed, int stepCount, int step_dev) {

        Thread clinetThread = new Thread() {
            public void run() {
                try {
                    double startTime = System.currentTimeMillis();
                    c_socket = new Socket(serverIP, port);
                    setSocket(c_socket);

                    setUserData(latitude, longitude, x, y, z, svm, speed, stepCount, step_dev);
                    sendWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_Socket.getOutputStream())), true);
                    br = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
                    sendWriter.println(userDataStr);
                    sendWriter.flush();
                    setActtype(br.readLine());
                    System.out.println("send" + getActtype());
                    sendWriter.close();
                    m_Socket.close();
                    double endTime = System.currentTimeMillis();
                    double diffTime = (endTime - startTime) / 1000;
                    System.out.println("[evalTime]: " + diffTime);
//                        SendThread send_thread = new SendThread(ds.getLat(), ds.getLon(), ds.getX(), ds.getY(), ds.getZ(), ds.getSvm(), ds.getSpeed(), ds.getStep(), ds.getStep_dev());
//
//                        send_thread.start();

//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clinetThread.start();
//        return true;
        return acttype;
    }
    public void setSocket(Socket socket) {
        m_Socket = socket;
    }
}

