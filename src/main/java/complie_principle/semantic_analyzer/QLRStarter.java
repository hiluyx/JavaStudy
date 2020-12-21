package complie_principle.semantic_analyzer;

import complie_principle.grammar_analyzer.GStarter;
import complie_principle.grammar_analyzer.LRParser;
import complie_principle.lexical_analyzer.CodeScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class QLRStarter {
    static CodeScanner codeScanner = CodeScanner.getInstance(codes());

    static char[] codes() {
        char[] tempChars = new char[10240];
        try {
            String appPath = (GStarter.class.getClassLoader().getResource("")+ "cp_data/app.txt").substring(6);
            File appFile = new File(appPath);
            FileReader reader = new FileReader(appFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            int i = bufferedReader.read(tempChars);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempChars;
    }
    public static void main(String[] args) {
        QuaternionLRParser lrParser = new QuaternionLRParser(codeScanner);
        lrParser.lrParser();
        for (Quaternion q : QuaternionLRParser.quaternions) {
            System.out.println(q.toString());
        }
    }
}
