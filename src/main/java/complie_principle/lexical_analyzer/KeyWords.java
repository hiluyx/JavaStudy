package complie_principle.lexical_analyzer;


public enum KeyWords {

    BEGIN(1,"begin"),
    IF(2,"if"),
    THEN(3,"then"),
    WHILE(4,"while"),
    DO(5,"do"),
    END(6,"end");

    private final int syn;
    private final String token;

    public int getSyn() {
        return this.syn;
    }

    public String getToken() {
        return this.token;
    }

    KeyWords(int syn,String token) {
        this.syn = syn;
        this.token = token;
    }

}
