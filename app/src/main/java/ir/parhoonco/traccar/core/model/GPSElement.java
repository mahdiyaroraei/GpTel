package ir.parhoonco.traccar.core.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by mao on 8/7/2016.
 */
public class GPSElement {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private long codecId;

    @DatabaseField
    private long timeStamp;

    @DatabaseField
    private long elementCount;

    @DatabaseField
    private long validElements;

    @DatabaseField
    private long differentialCoords;

    @DatabaseField
    private double longitude;

    @DatabaseField
    private double latitude;

    @DatabaseField
    private long speed;

    @DatabaseField
    private long imei;

    @DatabaseField
    private double battery;

    @DatabaseField
    private double temperature;

    @DatabaseField(dataType = DataType.DATE)
    private Date date;

    public GPSElement(long codecId, long timeStamp, long elementCount, long validElements
            , long differentialCoords, double longitude, double latitude, long speed
            , long imei , double battery , double temperature) {
        this.codecId = codecId;
        this.timeStamp = timeStamp;
        this.elementCount = elementCount;
        this.validElements = validElements;
        this.differentialCoords = differentialCoords;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.imei = imei;
        this.battery = battery;
        this.temperature = temperature;
        this.date = new Date(System.currentTimeMillis());
    }

    public GPSElement(double longitude, double latitude, long speed , Date date) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.date = date;
    }

    public GPSElement() {
    }
}
