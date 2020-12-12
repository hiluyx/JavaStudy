package complie_principle.grammar_analyzer;

import complie_principle.lexical_analyzer.CodeScanner;

import java.io.*;

public class GStarter {

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
        LRParser lrParser = new LRParser(codeScanner);
        lrParser.lrParser();
    }


}
