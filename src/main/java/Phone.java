import lombok.Data;

@Data
public class Phone {

    //屏幕宽
    private double screenWidth = 9;
    //屏幕高
    private double screenHeight = 16;

    private pointProject2Center pointProject2Center = new pointProject2Center();
    private Gyro gyro = new Gyro();

    /**
     *
     * @param type 0-x, 1-y, 2-z
     * @param degree 倾斜角度
     */
    public void tilt(int type, double degree) throws Exception {
        switch (type) {
            case 0: gyro.setXInclination(gyro.getXInclination()+degree);
            case 1: gyro.setYInclination(gyro.getYInclination()+degree);
            case 2: gyro.setZInclination(gyro.getZInclination()+degree);
            default: throw new Exception("type error???");
        }
    }

    /**
     * //是否能在手机屏幕上显示
     * @return 布尔值
     */
    public boolean isDisplayOnPhoneScreen(){
        return (pointProject2Center.getX() < screenWidth / 2)
                & (pointProject2Center.getY() < screenHeight / 2);
    }

    /**
     * 投影到手机屏幕上的点，以Center为坐标原点
     */
    @Data
    static class pointProject2Center {
        /*
        ---------------
        | point  .    |
        |        |  y |
        |Center  |    |  phone screen
        |      .--    |
        |       x     |
        |             |
        |             |
        ---------------
         */
        double x = 0;
        double y = 0;
    }

    /**
     * 陀螺仪
     */
    @Data
    static class Gyro {
        //x 倾角
        double xInclination;
        //y 倾角
        double yInclination;
        //z 倾角
        double zInclination;
    }
}
