package ir.parhoonco.traccar.core;

/**
 * Created by mao on 8/4/2016.
 */
public final class UnitsConverter {

    private UnitsConverter() {
    }

    public static double knotsFromKph(double value) { // km/h
        return value * 0.539957;
    }

    public static double knotsFromMph(double value) {
        return value * 0.868976;
    }

    public static double knotsFromMps(double value) { // m/s
        return value * 1.94384;
    }

    public static double knotsFromCps(double value) { // cm/s
        return value * 0.0194384449;
    }

}