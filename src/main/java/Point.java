import lombok.Data;

@Data
public class Point {

    private static double EARTH_RADIUS = 6378.137;
    //纬度
    private double latitude;
    //经度
    private double longitude;

    /**
     * 计算两点的距离（单位：米）
     * @param anotherPoint 另一点
     * @return 距离
     */
    public double distance2Another(Point anotherPoint) {
        double radLat1  = anotherPoint.getLatitude() * Math.PI / 180.0;
        double radLat2  = this.latitude* Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = anotherPoint.getLongitude()* Math.PI / 180.0 - this.longitude* Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }
}
