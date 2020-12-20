package DM.irisDataDue;

import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IrisDataReader {
    static final boolean addLabel = false;

    enum IrisClass {
        Iris_setosa("Iris-setosa",0),
        Iris_versicolor("Iris-versicolor",1),
        Iris_virginica("Iris-virginica",2);
        String class_iris;
        int label;
        IrisClass(String class_iris,int label) {
            this.class_iris=class_iris;
            this.label=label;
        }
        static IrisClass getIrisClass(String class_iris) {
            if (class_iris == null || class_iris.equals("")) return null;
            for (IrisClass irisClass : IrisClass.values()) {
                if (irisClass.getClass_iris().equals(class_iris)) {
                    return irisClass;
                }
            }
            return null;
        }
        String getClass_iris() {
            return class_iris;
        }
        int getLabel() {
            return this.label;
        }
    }

    @Data
    static class Iris {
        List<Double> irisDataList = new ArrayList<>();
        String class_Iris;
        boolean isMissing[] = new boolean[4];

        Iris(List<String> originStr) {
            for (int i = 0;i < 4; i ++) {
                double v = Double.parseDouble(originStr.get(i));
                if (!isMissing[i] && v < 0) {
                    isMissing[i] = true;
                }
                irisDataList.add(v);
            }
            class_Iris=originStr.get(4);
        }

        public void preprocessMissingD(int index, double newD) {
            irisDataList.remove(index);
            irisDataList.add(index,newD);
        }

        public String toString() {
            StringBuilder stringBuffer = new StringBuilder();
            for (double d: irisDataList) {
                stringBuffer.append(d).append(",");
            }
            stringBuffer.append(class_Iris);
            if (addLabel) stringBuffer.append(",").append(IrisClass.getIrisClass(class_Iris).getLabel());
            return stringBuffer.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> list = readLine();
        List<Iris> irises = new ArrayList<>();
        for (String s : list) {
            String[] split = s.split(",");
            irises.add(new Iris(Arrays.asList(split)));
        }
        for (int i = 0; i < 4; i++) {
            double avg = 0;
            double numI = 0;
            for (Iris iris: irises) {
                if (!iris.getIsMissing()[i]) {
                    avg += iris.getIrisDataList().get(i);
                    numI ++;
                }
            }
            avg /= numI;
            int a_ = (int)avg * 10;
            avg = a_/10.0;
            for (Iris iris: irises) {
                if (iris.getIsMissing()[i]) {
                    iris.preprocessMissingD(i,avg);
                }
            }
        }
        System.out.println("..............");
        for (Iris iris: irises) System.out.println(iris.toString());
    }
    static List<String> readLine() throws IOException {
        List<String> list = new ArrayList<>();
        String irisPath =
                (IrisDataReader.class.getClassLoader().getResource("") + "DM/iris.data").substring(6);
        File file = new File(irisPath);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            if (line.length() != 0) {
                list.add(line);
            }
            line = bufferedReader.readLine();
        }
        return list;
    }



    static void writeLine(List<Iris> irises) {

    }
}
