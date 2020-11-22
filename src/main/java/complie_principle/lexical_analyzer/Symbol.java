package complie_principle.lexical_analyzer;

public enum Symbol {

    ADD(40,"+"),
    SUB(41,"-"),
    MUL(42,"*"),
    DIV(43,"/"),
    COL(44,":"),
    EQL(45,"="),
    MORE(46,">"),
    LESS(47,"<"),

    COL_EQL(48,":="),
    ADD_EQL(49,"+="),
    SUB_QEL(50,"-="),
    DIV_EQL(51,"/="),
    MUL_EQL(52,"*="),
    EQL_DOU(53,"=="),
    UN_EQL(54,"!="),
    LESS_EQL(55,"<="),
    MORE_EQL(56,">="),

    LESS_MORE(57,"<>"),
    SEM(58,";"),
    LPA(59,"("),
    RPA(60,")"),
    POINT(61,"."),

    ERROR(-1,"ERROR");

    private final int syn;
    private final String sign;

    public int getSyn() {
        return syn;
    }

    public String getSign() {
        return sign;
    }

    Symbol(int syn, String sign) {
        this.sign = sign;
        this.syn = syn;
    }

    public static Symbol getSymbol(String sign) {
        if (sign == null || sign.equals("")) return ERROR;
        for (Symbol symbol : Symbol.values()) {
            if (symbol.getSign().equals(sign)) {
                return symbol;
            }
        }
        return ERROR;
    }
}
