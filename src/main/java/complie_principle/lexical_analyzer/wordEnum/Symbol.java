package complie_principle.lexical_analyzer.wordEnum;

public enum Symbol {

    CODE_END(0,"#"),
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
    LCB(62,"{"),
    RCB(63,"}"),
    COM(64,","),
    D_QUO(65,"\""),
    QUO(66,"'"),
    LS_BR(67,"["),
    RS_BR(68,"]"),
    OR(69,"|"),
    AND(70,"&"),
    SLA(71,"\\");

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
        if (sign == null || sign.equals("")) return null;
        for (Symbol symbol : Symbol.values()) {
            if (symbol.getSign().equals(sign)) {
                return symbol;
            }
        }
        return null;
    }
}
