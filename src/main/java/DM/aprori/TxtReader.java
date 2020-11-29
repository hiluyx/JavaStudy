package DM.aprori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class TxtReader {

    public static List<List<String>> getAproriRecord(){
        List<List<String>> record = new ArrayList<>();
        try {
            String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
            String filePath = Objects.requireNonNull(TxtReader.class.getClassLoader().getResource("DM/aprori_data.txt")).toString().substring(6);
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT;
                while ((lineTXT = bufferedReader.readLine()) != null) {//读一行文件
                    String[] lineString = lineTXT.split(" ");
                    List<String> lineList = new ArrayList<>();//(Arrays.asList(lineString).subList(1, lineString.length));
                    Collections.addAll(lineList, lineString);
                    record.add(lineList);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件！");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容操作出错");
            e.printStackTrace();
        }
        return record;
    }

}
