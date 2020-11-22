package complie_principle.lexical_analyzer;

/**
 * 代码分析器
 * codes 代码缓冲区
 * token 单词缓冲区
 */
public class CodeScanner {

    private final char[] codes;
    private final char[] token = new char[255];
    private int p_input = 0;
    private int p_token = 0;

    private char ch;

    public CodeScanner(char[] codes) {
        this.codes = codes;
    }

    public Word scan() {
        p_token = 0;
        m_get_ch();
        get_bc();
        if (letter()) {
            //System.out.println("----letter");
            while (letter() || digit()) {
                //System.out.print("  +"+ch+", ");
                concat();
                m_get_ch();
            }
            retract();
            return new Word(reserve(),String.copyValueOf(token));
        } else if (digit()) {
            //System.out.println("----digit");
            while (digit()) {
                //System.out.print("  +"+ch+", ");
                concat();
                m_get_ch();
            }
            retract();
            return new Word(11,String.copyValueOf(token));
        } else {
            //System.out.println("----not digit or letter");
            if (ch == '=' || ch == '>' || ch == '<' || ch == '!' || ch == '+' || ch == '-' || ch == '*') { // 可能的双符号拼接
                //System.out.println("    ----StitchSymbol");
                return getStitchSymbol();
            }else if (ch == '/') { // 可能出现注释
                //System.out.println("    ----comment");
                m_get_ch();
                if (ch == '/') { // 单行注释，注释后面紧接着\n和\t
                    //System.out.println("        ----single comment");
                    m_get_ch();
                    while ((int)ch != 13) {
                        m_get_ch();
                    }
                    m_get_ch(); // 把\t去掉
                } else if (ch == '*') { // 多行注释
                    //System.out.println("        ----more comments");
                    while (true) {
                        m_get_ch();
                        if (ch == '*') {
                            m_get_ch();
                            if (ch == '/') {
                                break;
                            }
                        }
                    }
                } else { // 不是注释，而是/=，进行双符号拼接
                    //System.out.println("        ----StitchSymbol");
                    retract();
                    return getStitchSymbol();
                }
                return scan();
            } else if((int)ch == 13) { // 处理\n与\t
                //System.out.println("    ----遇见换行");
                m_get_ch(); // 把\t去掉
                return scan();
            }else { // 单符号
                //System.out.println("    ----single symbol");
                if ((int)ch == 0) {
                    System.out.println("文件结束");
                    return new Word(-1,"end");
                }
                return new Word(Symbol.getSymbol(String.valueOf(ch)));
            }
        }
    }

    private Word getStitchSymbol() {
        char[] str = new char[2];
        str[0] = ch;
        m_get_ch();
        if (ch == '=') { // 双符号拼接
            str[1] = '=';
        }
        // 单符号
        retract();
        return new Word(Symbol.getSymbol(String.valueOf(str[0])));
    }

    private void m_get_ch() {
        ch = codes[p_input];
        p_input ++;
    }

    private void get_bc() {
        while (ch == ' ' || ch == 10) {
            ch = codes[p_input];
            p_input ++;
        }
    }

    private void concat() {
        token[p_token] = ch;
        p_token ++;
        token[p_token] = '\0';
    }

    private boolean letter() {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    private boolean digit() {
        return ch >= '0' && ch <='9';
    }

    private int reserve() {
        for (KeyWords k : KeyWords.values()) {
            if (k.getToken().equals(String.copyValueOf(token))) {
                return k.getSyn();
            }
        }
        return 10;
    }

    private void retract() {
        p_input --;
    }
}
