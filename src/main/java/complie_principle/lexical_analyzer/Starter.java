package complie_principle.lexical_analyzer;

import complie_principle.lexical_analyzer.exception.WordException;
import complie_principle.lexical_analyzer.wordEnum.Word;

import java.io.*;

public class Starter {
    public static void main(String[] args) throws IOException, WordException {
        String appPath = (Starter.class.getClassLoader().getResource("")+ "cp_data/app.txt").substring(6);
        File appFile = new File(appPath);
        FileReader reader = new FileReader(appFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        char[] tempChars = new char[10240];
        int i = bufferedReader.read(tempChars);
//        System.out.println(i);
//        System.out.println("----------------------");
//        System.out.println(tempChars);
//        System.out.println("----------------------");
        CodeScanner scanner = new CodeScanner(tempChars);
        Word word = scanner.scan();
        while (word.getTypeNum() != 0) {
            System.out.println("("+word.getTypeNum()+", '"+word.getWord()+"')");
            word = scanner.scan();
            if (word.getTypeNum() == -1) {
                throw new WordException("Illegal word: "+word.getWord());
            }
        }
        System.out.println("文件分析结束。");
    }
}
