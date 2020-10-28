public class Main {
    private static final Phone myMiPhone = new Phone();
    private static final Point myPoint = new Point();
    private static final Point destination = new Point();
    public static void main(String[] args) {
        //初始化位置
        myPoint.setLatitude(23.345);
        myPoint.setLongitude(112.553);
        destination.setLongitude(112.766);
        destination.setLatitude(23.441);

        System.out.println(myPoint.distance2Another(destination));

    }

    /**
     * 投影算法
     */
    public void projection() {

    }
}
