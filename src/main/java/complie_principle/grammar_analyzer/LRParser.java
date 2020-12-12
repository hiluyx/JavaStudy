package complie_principle.grammar_analyzer;

import complie_principle.lexical_analyzer.CodeScanner;
import complie_principle.lexical_analyzer.wordEnum.Word;

/**
 * 递归下降分析程序
 */
public class LRParser {
    static int errorNum = 0;
    static int lineNum = 1;
    public static int syn = 1;
    private final CodeScanner codeScanner;
    public LRParser(CodeScanner codeScanner) {
        this.codeScanner = codeScanner;
    }

    void lrParser() {
        syn = codeScanner.scan().getTypeNum();
        System.out.print("分析第"+lineNum+"句 ");
        if (syn != 1) {
            errorNum++;
            System.out.print("缺begin");
        } else {
            syn = codeScanner.scan().getTypeNum();
        }

        System.out.println();
        lineNum ++;

        yucu();

        if (syn == 6) {
            syn = codeScanner.scan().getTypeNum();
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
            syn = codeScanner.scan().getTypeNum();
            if (statement()) break;
        }
    }

    // 返回bool确定是否为end句
    boolean statement() {
        System.out.print("分析第"+lineNum+"句 ");
        if (syn == 20) {
            syn = codeScanner.scan().getTypeNum();
            if (syn == 48) {
                syn = codeScanner.scan().getTypeNum();
                expression();
            } else {
                errorNum++;
                System.out.print("赋值号错误");
                jumpTo58();
            }

            System.out.println();
            return false;
        } else if (syn == 6 || syn == 0) {
            return true;
        } else {
            errorNum++;
            System.out.print("语句错误");
            jumpTo58();

            System.out.println();
            return false;
        }
    }

    void expression() {
        term();
        while (syn == 40 || syn == 41) {
            syn = codeScanner.scan().getTypeNum();
            term();
        }
    }

    void term() {
        factor();
        while (syn == 42 || syn == 43) {
            syn = codeScanner.scan().getTypeNum();
            factor();
        }
    }

    void factor() {
        if (syn == 20 || syn == 21) {
            syn = codeScanner.scan().getTypeNum();
        } else if (syn == 59) {
            syn = codeScanner.scan().getTypeNum();
            expression();
            if (syn == 60) {
                syn = codeScanner.scan().getTypeNum();
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
    }

    void jumpTo58() {
        if (syn == 58) return; // 空语句，直接返回
        Word scan = codeScanner.scan();
        syn = scan.getTypeNum();
        while(syn != 58) {
            Word scan1 = codeScanner.scan();
            syn = scan1.getTypeNum();
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
