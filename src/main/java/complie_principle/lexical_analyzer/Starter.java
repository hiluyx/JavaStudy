package complie_principle.lexical_analyzer;

import java.io.*;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws IOException {
        String appPath = (Starter.class.getClassLoader().getResource("")+"app.txt").substring(6);
        File appFile = new File(appPath);
        FileReader reader = new FileReader(appFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        char[] tempChars = new char[1024];
        int i = bufferedReader.read(tempChars);
        System.out.println(i);
        System.out.println("----------------------");
        System.out.println(tempChars);
        System.out.println("----------------------");
        CodeScanner scanner = new CodeScanner(tempChars);
        Word word = scanner.scan();
        while (word.getTypeNum() != -1) {
            System.out.println("("+word.getTypeNum()+", '"+word.getWord()+"')");
            word = scanner.scan();
        }
    }
}
