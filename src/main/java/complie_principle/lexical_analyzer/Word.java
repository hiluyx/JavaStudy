package complie_principle.lexical_analyzer;

import lombok.Data;

@Data
public class Word {

    private int typeNum;
    private String word;

    public Word(){

    }

    public Word(int typeNum, String word) {
        this.typeNum = typeNum;
        this.word = word;
    }

    public Word(KeyWords keyWords) {
        this.typeNum = keyWords.getSyn();
        this.word = keyWords.getToken();
    }

    public Word(Symbol symbol) {
        this.setTypeNum(symbol.getSyn());
        this.setWord(symbol.getSign());
    }
}
