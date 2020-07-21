package ksnu.dsem.realtimeactivityrecognition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveLog {
    public SaveLog(String text){
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            System.out.print("완료");
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // 적절한 예외처리를 해주면됩니다.
            e.printStackTrace();
        }
        }


}
