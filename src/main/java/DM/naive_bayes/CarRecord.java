package DM.naive_bayes;

import lombok.Data;

@Data
public class CarRecord {
    private String price;
    private String mtFees;
    private String carDoor;
    private String carrier;
    private String bodySize;
    private String safety;

    public CarRecord(String price, String mtFees, String carDoor, String carrier, String bodySize, String safety) {
        this.price = price;
        this.mtFees = mtFees;
        this.carDoor = carDoor;
        this.carrier = carrier;
        this.bodySize = bodySize;
        this.safety = safety;
    }
}
