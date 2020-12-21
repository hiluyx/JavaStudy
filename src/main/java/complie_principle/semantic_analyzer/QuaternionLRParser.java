package complie_principle.semantic_analyzer;

import complie_principle.lexical_analyzer.CodeScanner;
import complie_principle.lexical_analyzer.wordEnum.Symbol;
import complie_principle.lexical_analyzer.wordEnum.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuaternionLRParser {

    private Word word;
    public static final List<Quaternion> quaternions = new ArrayList<>();
    static int errorNum = 0;
    static int lineNum = 1;
    public static int syn = 1;
    private final CodeScanner codeScanner;
    public QuaternionLRParser(CodeScanner codeScanner) {
        this.codeScanner = codeScanner;
    }

    void lrParser() {
        word = codeScanner.scan();
        syn = word.getTypeNum();
        System.out.print("分析第"+lineNum+"句 ");
        if (syn != 1) {
            errorNum++;
            System.out.print("缺begin");
        } else {
            word = codeScanner.scan();
            syn = word.getTypeNum();
        }

        System.out.println();
        lineNum ++;

        yucu();

        if (syn == 6) {
            word = codeScanner.scan();
            syn = word.getTypeNum();
            print(syn == 0 && errorNum == 0);
        } else {
            errorNum++;
            System.out.println("缺end");
            print(false);
        }
    }

    void yucu() {
        if (statement()) return;
        while (syn == 58) {
            //System.out.println("分析第"+lineNum+"句");
            lineNum ++;
            word = codeScanner.scan();
            syn = word.getTypeNum();
            if (statement()) break;
        }
    }

    // 返回bool确定是否为end句
    boolean statement() {
        System.out.print("分析第"+lineNum+"句 ");
        String tt,ePlace;
        switch (syn) {
            case 20 : {
                tt = word.getWord();
                word = codeScanner.scan();
                syn = word.getTypeNum();
                if (syn == 48) {
                    word = codeScanner.scan();
                    syn = word.getTypeNum();
                    ePlace = expression();
                    quaternions.add(new Quaternion(tt,ePlace,"",""));
                } else {
                    errorNum++;
                    System.out.print("赋值号错误");
                    jumpTo58();
                }

                System.out.println();
                return false;
            }
            case 6 :
            case 0 : return true;
            default: {
                errorNum++;
                System.out.print("语句错误");
                jumpTo58();

                System.out.println();
                return false;
            }
        }
    }

    String expression() {
        String tp,ep2,ePlace,tt;
        ePlace = term();
        while (syn == 40 || syn == 41) {
            tt = Objects.requireNonNull(Symbol.getSymbol(syn)).getSign();
            word = codeScanner.scan();
            syn = word.getTypeNum();
            ep2 = term();
            tp = Quaternion.newTemp();
            quaternions.add(new Quaternion(tp,ePlace,tt,ep2));
            ePlace = tp;
        }
        return ePlace;
    }

    String term() {
        String tp,ep2,ePlace,tt;
        ePlace = factor();
        while (syn == 42 || syn == 43) {
            tt = Objects.requireNonNull(Symbol.getSymbol(syn)).getSign();
            word = codeScanner.scan();
            syn = word.getTypeNum();
            ep2 = factor();
            tp = Quaternion.newTemp();
            quaternions.add(new Quaternion(tp,ePlace,tt,ep2));
            ePlace = tp;
        }
        return ePlace;
    }

    String factor() {
        String fPlace = "";
        if (syn == 20 || syn == 21) {
            fPlace = word.getWord();
            word = codeScanner.scan();
            syn = word.getTypeNum();
        } else if (syn == 59) {
            word = codeScanner.scan();
            syn = word.getTypeNum();
            fPlace = expression();
            if (syn == 60) {
                word = codeScanner.scan();
                syn = word.getTypeNum();
            } else {
                errorNum++;
                System.out.print("‘)’错误");
                jumpTo58();
            }
        } else {
            errorNum++;
            System.out.print("表达式错误");
            jumpTo58();
        }
        return fPlace;
    }

    void jumpTo58() {
        if (syn == 58) return; // 空语句，直接返回
        word = codeScanner.scan();
        syn = word.getTypeNum();
        while(syn != 58) {
            word = codeScanner.scan();
            syn = word.getTypeNum();
            if (syn == 6 ||syn == 0) {
                return;
            }
        }
    }

    void print(boolean isSuccess) {
        String print = isSuccess?"success: " : "failed: ";
        System.out.println(
                "\n--------------------------------\n" +
                        print + "error("+ errorNum+")\n" +
                        "--------------------------------");
    }
}
