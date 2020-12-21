package complie_principle.semantic_analyzer;

import lombok.Data;

@Data
public class Quaternion {
    public static int SERIAL_NUMBER = 0;
    String result;
    String ag1;
    String op;
    String ag2;

    public Quaternion(String result, String ag1, String op, String ag2) {
        this.result = result;
        this.ag1 = ag1;
        this.op = op;
        this.ag2 = ag2;
    }

    @Override
    public String toString() {
        return result +"="+ ag1 + op + ag2;
    }

    public static String newTemp() {
        return "t"+SERIAL_NUMBER++;
    }
}
