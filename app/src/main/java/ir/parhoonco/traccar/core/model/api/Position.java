package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by mao on 9/4/2016.
 */
public class Position extends SugarRecord{
    @Unique
    int positionid;
    int altitude;
    String address;
    double latitude;
    double longtitude;
    long servertime;
    long fixtime;
    long devicetime;
    float speed;
    Device device;

    public Position() {
    }

    public Position(int positionid, int altitude, String address, double latitude, double longtitude, long servertime, long fixtime, long devicetime, float speed) {
        this.positionid = positionid;
        this.altitude = altitude;
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.servertime = servertime;
        this.fixtime = fixtime;
        this.devicetime = devicetime;
        this.speed = speed;
    }

    public int getPositionid() {
        return positionid;
    }

    public void setPositionid(int positionid) {
        this.positionid = positionid;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double latitude) {
        this.latitude = latitude;
    }

    public double getLon() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public long getServertime() {
        return servertime;
    }

    public void setServertime(long servertime) {
        this.servertime = servertime;
    }

    public long getFixtime() {
        return fixtime;
    }

    public void setFixtime(long fixtime) {
        this.fixtime = fixtime;
    }

    public long getDevicetime() {
        return devicetime;
    }

    public void setDevicetime(long devicetime) {
        this.devicetime = devicetime;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
