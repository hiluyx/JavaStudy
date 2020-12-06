package DM.naive_bayes;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class CarRecord {

    private List<String> attributes = new ArrayList<>();
    private int aLength;
    public CarRecord(String[] l) {
        attributes.addAll(Arrays.asList(l));
        aLength = l.length;
    }
}
