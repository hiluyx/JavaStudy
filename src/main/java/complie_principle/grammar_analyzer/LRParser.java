package complie_principle.grammar_analyzer;

import complie_principle.lexical_analyzer.CodeScanner;

/**
 * 递归下降分析程序
 */
public class LRParser {
    static int lineNum = 1;
    public static int syn = 1;
    static int kk;
    private final CodeScanner codeScanner;
    public LRParser(CodeScanner codeScanner) {
        this.codeScanner = codeScanner;
    }

    void lrParser() {
        syn = codeScanner.scan().getTypeNum();
        if (syn == 1) {
            syn = codeScanner.scan().getTypeNum();
            yucu();
            if (syn == 6) {
                syn = codeScanner.scan().getTypeNum();
                if (syn == 0 && kk == 0) System.out.println("success");
            } else {
                if (kk != 1) {
                    System.out.print("分析第"+lineNum+"行 ");
                    System.out.println("缺end");
                    kk = 1;
                }
            }
        } else {
            System.out.print("分析第"+lineNum+"行 ");
            System.out.println("缺begin");
            jump();
            kk = 1;
        }
    }

    void yucu() {
        statement();
        while (syn == 58) {
            System.out.println("分析第"+lineNum+"行");
            lineNum ++;
            syn = codeScanner.scan().getTypeNum();
            statement();
        }
    }

    void statement() {
        if (syn == 20) {
            syn = codeScanner.scan().getTypeNum();
            if (syn == 48) {
                syn = codeScanner.scan().getTypeNum();
                expression();
            } else {
                System.out.print("分析第"+lineNum+"行 ");
                System.out.println("赋值号错误");
                kk = 1;
                jump();
            }
        } else {
            System.out.print("分析第"+lineNum+"行 ");
            System.out.println("语句错误");
            kk = 1;
            jump();
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
                System.out.print("分析第"+lineNum+"行 ");
                System.out.println("‘)’错误");
                kk = 1;
                jump();
            }
        } else {
            System.out.print("分析第"+lineNum+"行 ");
            System.out.println("表达式错误");
            kk = 1;
            jump();
        }
    }

    boolean jump() {
        syn = codeScanner.scan().getTypeNum();
        while(syn != 58) {
            syn = codeScanner.scan().getTypeNum();
            if (syn == 6) {
                return false;
            }
        }
        return true;
    }
}
