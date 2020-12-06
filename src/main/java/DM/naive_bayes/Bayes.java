package DM.naive_bayes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Bayes {
    static List<CarRecord> testLs = new ArrayList<>(); // 输入测试
    static List<CarRecord> classRecordList_un_acc = new ArrayList<>(); // 评价为un_acc的汽车记录
    static List<CarRecord> classRecordList_acc = new ArrayList<>(); //
    static List<CarRecord> classRecordList_good = new ArrayList<>(); //
    static List<CarRecord> classRecordList_v_good = new ArrayList<>(); //

    static double p_un_acc;
    static double p_acc;
    static double p_good;
    static double p_v_good;

    enum Predict{
        p_un_acc(0,"p_un_acc"),p_acc(1,"p_acc"),p_good(2,"p_good"),p_v_good(3,"p_v_good");
        int index;
        String pre;
        Predict(int index, String pre) {
            this.index = index;
            this.pre = pre;
        }
        String getPredict() {
            return pre;
        }
    }

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

        testLs.add(new CarRecord(new String[]{"low","low","4","4","small","high"}));
        testLs.add(new CarRecord(new String[]{"high","high","4","4","med","med"}));
        testLs.add(new CarRecord(new String[]{"low","high","3","2","small","low"}));
        testLs.add(new CarRecord(new String[]{"low","low","2","more","big","high"}));

        for (CarRecord record : testLs) {
            double un_acc_p = bayes(record,classRecordList_un_acc) * p_un_acc;
            double acc_p = bayes(record,classRecordList_acc) * p_acc;
            double good_p = bayes(record,classRecordList_good) * p_good;
            double v_good_p = bayes(record,classRecordList_v_good) * p_v_good;
            double[] p_ss = {un_acc_p,acc_p,good_p,v_good_p};
            double max = un_acc_p;
            int maxInt = 0;
            for (int i = 0;i < p_ss.length;i ++) {
                if (p_ss[i] > max) {
                    max = p_ss[i];
                    maxInt = i;
                }
            }
            String preStr ;
            if (max != 0) {
                preStr = Predict.values()[maxInt].getPredict();
            } else {
                preStr = "unknown";
            }System.out.println(record.toString()+
                    "\n\t\tpredict: " + preStr +", p = " + p_ss[maxInt]);
        }
    }

    static void pretreatment(List<String> data) throws Exception {
        for (String c : data) {
            String[] c_s = c.split(",");
            if (c_s.length < 7) throw new Exception("data error??");
            CarRecord addE = new CarRecord(c_s);
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
        double[] p_S = new double[carRecord.getALength()];
        for (int i = 0;i < carRecord.getALength();i ++) {
            String s = carRecord.getAttributes().get(i);
            for (CarRecord record : similarClassList) {
                if (record.getAttributes().get(i).equals(s)) {
                    p_S[i] ++;
                }
            }
        }
        double re_P = 1.0;
        for (double d : p_S) {
            re_P *= d / similarClassList.size();
        }
        return re_P;
    }
}