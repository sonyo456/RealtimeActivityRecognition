package ksnu.dsem.realtimeactivityrecognition;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ModelClient {    //메인 activity 시작!
    private String serverIP = "203.234.62.144";
    private int port = 12020;
    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    String userId;
    public static final int DEFAULT_BUFFER_SIZE = 2048;//

    long totalReadBytes = 0;
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int readBytes;
    double startTime = 0;

    public void sendId(String userId, String dirPath) {
        File dir = new File(dirPath);
        //파일 필터 설정-log로 시작하는 파일
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().startsWith("Collect");
//                return f.getName().startsWith("Log");
            }
        };

        // 필터 적용
        File files[] = dir.listFiles(filter);
        for (int i = 0; i < files.length; i++) {

            File file = new File(String.valueOf(files[i]));
            System.out.println("FILE" + String.valueOf(files[i]));
            if (!file.exists()) {
                System.out.println("File not Exist.");
                System.exit(0);
            }

            long fileSize = file.length();

            Thread clientThread = new Thread() {
                public void run() {
                    try {
                        Socket socket = new Socket(serverIP, port);
                        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        FileInputStream fis = new FileInputStream(file);

                        if (!socket.isConnected()) {
                            System.out.println("Connect Error.");
                            System.exit(0);
                        }

//                        startTime = System.currentTimeMillis();
                        OutputStream os = socket.getOutputStream();
                        //바이트로 변환
                        if (userId != null) { //만약 데이타가 아무것도 입력된 것이 아니라면
                            out.println(userId); //data를   stream 형태로 변형하여 전송.  변환내용은 쓰레드에 담겨 있다.
                            while ((readBytes = fis.read(buffer)) > 0) {
                                os.write(buffer, 0, readBytes);
                                totalReadBytes += readBytes;
                                System.out.println("In progress: " + totalReadBytes + "/"
                                        + fileSize + " Byte(s) ("
                                        + (totalReadBytes * 100 / fileSize) + " %)");
                                if ((totalReadBytes * 100 / fileSize) == 100) {
                                    socket.close();
                                    ;
                                }
                            }

                            //전송후 파일 삭제
//                            file.delete();
//                            Socket socket = new Socket(serverIP, port);
//                            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
//                            DataInputStream dis = new DataInputStream(bis);
//
//                            String filename = dis.readUTF();
//                            System.out.println("gasdfdfs " + filename);
//                            long fileLength = dis.readLong();
//                            FileOutputStream fos = new FileOutputStream(new File("/data/data/ksnu.dsem.realtimeactivityrecognition/files/", filename));
//                            BufferedOutputStream bos = new BufferedOutputStream(fos);
//                            for (int j = 0; j < fileLength; j++) { //파일 길이 만큼 읽습니다.
//                                bos.write(bis.read());
//                            }
//
//
//                            bos.flush();
                        }
                        System.out.println("File transfer completed.");
                        fis.close();
                        os.close();
                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };


            double endTime = System.currentTimeMillis();
            double diffTime = (endTime - startTime) / 1000;
            ;
            double transferSpeed = (fileSize / 1000) / diffTime;

            System.out.println("time: " + diffTime + " second(s)");
            System.out.println("Average transfer speed: " + transferSpeed + " KB/s");


            clientThread.start();
            recieveThread.start();
        }


    }
    Thread recieveThread = new Thread() {
        public void run() {
            System.out.println("Connect Error.");
            try {
                Socket socket = new Socket(serverIP, port);

                if (!socket.isConnected()) {
                    System.out.println("Connect Error.");
                    System.exit(0);
                }

                startTime = System.currentTimeMillis();

                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                DataInputStream dis = new DataInputStream(bis);

                String filename = dis.readUTF();
                System.out.println("gasdfdfs " + filename);
                long fileLength = dis.readLong();
                FileOutputStream fos = new FileOutputStream(new File("/data/data/ksnu.dsem.realtimeactivityrecognition/files/", filename));
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                for (int j = 0; j < fileLength; j++) { //파일 길이 만큼 읽습니다.
                    bos.write(bis.read());
                }


                bos.flush();
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("File transfer completed.");


        }
    };
}
