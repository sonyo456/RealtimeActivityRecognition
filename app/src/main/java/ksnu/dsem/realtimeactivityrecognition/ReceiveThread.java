package ksnu.dsem.realtimeactivityrecognition;

import java.io.*;
import java.net.*;

public class ReceiveThread extends Thread{
    private Socket r_Socket;

    public void run() {
        super.run();
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(r_Socket.getInputStream()));
            String receiveString;
            while(true) {
                receiveString = buf.readLine();
                System.out.println("���� : " + receiveString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setSocket(Socket socket) {
        r_Socket = socket;
    }
}
