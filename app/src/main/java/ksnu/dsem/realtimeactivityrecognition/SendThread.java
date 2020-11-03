package ksnu.dsem.realtimeactivityrecognition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendThread extends Thread {
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

    public SendThread(double latitude, double longitude, double x, double y, double z, double svm, double speed, int stepCount, int step_dev) {
        updateCurrentTime();
        this.latitude = latitude;
        this.longitude = longitude;
        this.x = x;
        this.y = y;
        this.z = z;
        this.svm = svm;
        this.speed = speed;
        this.stepCount = stepCount;
        this.step_dev = step_dev;
    }

    public void updateCurrentTime() {
        long now = System.currentTimeMillis();
        Date dt = new Date(now);
        this.currtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
    }

    public void setUserData() {
        userDataStr = currtime + "," + latitude + "," + longitude + "," + x + "," + y + "," + z + "," + svm + "," + speed + "," + stepCount + "," + step_dev;
    }
    public void setActtype(String acttype){
        this.acttype = acttype;
    }

    public String getActtype() {
        return acttype;
    }

    public void run() {
        super.run();
        try {
            setUserData();
            sendWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_Socket.getOutputStream())), true);
            br = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
            sendWriter.println(userDataStr);
            sendWriter.flush();
            setActtype(br.readLine());
            System.out.println("send" + getActtype());
            sendWriter.close();
            m_Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        m_Socket = socket;
    }


}

