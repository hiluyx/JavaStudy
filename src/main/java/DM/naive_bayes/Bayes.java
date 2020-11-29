package DM.naive_bayes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Bayes {
    static List<List<CarRecord>> testLs = new ArrayList<>(); // 输入测试
    static List<CarRecord> classRecordList_un_acc = new ArrayList<>(); // 评价为un_acc的汽车记录
    static List<CarRecord> classRecordList_acc = new ArrayList<>(); //
    static List<CarRecord> classRecordList_good = new ArrayList<>(); //
    static List<CarRecord> classRecordList_v_good = new ArrayList<>(); //

    static double p_un_acc;
    static double p_acc;
    static double p_good;
    static double p_v_good;

    public static void main(String[] args) throws Exception {
        String url = (Bayes.class.getClassLoader().getResource("") + "DM/bayes_data.txt").substring(6);
        File file = new File(url);
        FileReader fileReader = new FileReader(file);
        BufferedReader bf = new BufferedReader(fileReader);
        String line = bf.readLine();

        List<String> dataList = new ArrayList<>();

        while(line!=null){
            if (!line.equals("")) {
                dataList.add(line);
            }
            line = bf.readLine();
        }

        pretreatment(dataList);
        double s = dataList.size();
        p_un_acc = classRecordList_un_acc.size() / s;
        p_acc = classRecordList_acc.size() / s;
        p_good = classRecordList_good.size() / s;
        p_v_good = classRecordList_v_good.size() / s;
    }

    static void pretreatment(List<String> data) throws Exception {
        for (String c : data) {
            String[] c_s = c.split(",");
            if (c_s.length < 7) throw new Exception("data error??");
            CarRecord addE = new CarRecord(
                    c_s[0],
                    c_s[1],
                    c_s[2],
                    c_s[3],
                    c_s[4],
                    c_s[5]);
            switch (c_s[6]) {
                case "unacc":
                    classRecordList_un_acc.add(addE);
                    break;
                case "acc":
                    classRecordList_acc.add(addE);
                    break;
                case "good":
                    classRecordList_good.add(addE);
                    break;
                default:
                    classRecordList_v_good.add(addE);
                    break;
            }
        }
    }

    static double bayes(CarRecord carRecord, List<CarRecord> similarClassList) {
        return 0;
    }
}



