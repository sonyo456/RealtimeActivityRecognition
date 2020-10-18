package ksnu.dsem.realtimeactivityrecognition;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class SendFile {

    public static File[] getFileList(String dirPath) {
        File dir = new File(dirPath);
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().startsWith("Log");
            }
        };

        File files[] = dir.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            System.out.println("file: " + files[i]);
        }
        return files;

    }
//    public void inputFileToDB(String path) {
////		ArrayList<String> filenames = getFileList(path);
////		System.out.println(filenames);
////		String[] file = new String[6];
//
////		for (int a = 0; a < filenames.size(); a++) {
////			String filename = filenames.get(a);
//        String filename  =  "/data/data/ksnu.dsem.realtimeactivityrecognition/files/Log"+fileName;;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(filename));
//            System.out.println(filename);
//            StringTokenizer st = new StringTokenizer(filename, "\\");
//            for (int j = 0; j < 1; j++) {
////					for (int i = 0; st.hasMoreTokens(); i++) {
////						file[i] = st.nextToken();
////					}
//                if (this.dbc == null) {
//                    System.out.println("gg");
//                    break;
//                }
//                Scanner sc = new Scanner(br);
//                String line = sc.nextLine();
//                while (sc.hasNext()) {
//                    line = sc.nextLine();
//
//                    String[] line_split = line.split(",");
////						String sys_date = line_split[0];
//                    int Id = Integer.parseInt(line_split[0]);
//                    String userid = line_split[1];
//                    String strDate = line_split[2];
////						System.out.println(strDate);
//                    String set_id = line_split[3];
//                    int set_seq =  Integer.parseInt(line_split[4]);
//                    double x = Double.parseDouble(line_split[5]);
//                    double y = Double.parseDouble(line_split[6]);
//                    double z = Double.parseDouble(line_split[7]);
//                    int hr = Integer.parseInt(line_split[8]);
//                    double lat = Double.parseDouble(line_split[9]);
//                    double lon = Double.parseDouble(line_split[10]);
//                    double alt = Double.parseDouble(line_split[11]);
//                    String acttype = line_split[12];
//                    String actaccu = line_split[13];
//                    dbc.lifelog_raw(Id, userid, strDate,set_id, set_seq, x, y, z, hr, lat, lon, alt, acttype, actaccu);
//
//                }
//                System.out.println(filename + "성공");
//            }
//            br.close();
//        } catch (Exception e) {
//            System.out.println(filename + "실패");
//
//        }
//    }

//	}
}
