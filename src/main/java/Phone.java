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
     * 向垂直方向倾斜手机
     * @param degree 调整度数
     */
    public void tilt2Vertical(double degree) {
        gyro.setVerticalTilt(gyro.getVerticalTilt()+degree);
    }

    /**
     * 向水平方向倾斜手机
     * @param degree 调整度数
     */
    public void tilt2Horizontal(double degree) {
        gyro.setHorizontalTilt(gyro.getHorizontalTilt()+degree);
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
        /*
        垂直倾斜度：
          零 说明手机面垂直雷达面；
        负数 说明手机的上部分向使用者倾斜；
        正数 说明手机的下部分向使用者倾斜。
         */
        double verticalTilt = 0;
        /*
        水平倾斜度：
          零 说明手机中线垂直于雷达面；
        负数 说明手机的中线向左侧倾斜；
        正数 说明手机的中线向右侧倾斜。
         */
        double horizontalTilt = 0;
    }
}
