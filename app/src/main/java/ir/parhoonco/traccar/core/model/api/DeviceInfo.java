package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/24/2016.
 */
public class DeviceInfo extends SugarRecord{
    int doorstatus;
    float odometer;
    String motion;
    boolean protectionmode;
    float temperature;
    boolean ismaster;
    int enginstatus;
    long lastupdate;
    int voltage;
    String status;

    public DeviceInfo() {
    }

    public DeviceInfo(int doorstatus, float odometer, String motion, boolean protectionmode, float temperature, boolean ismaster, int enginstatus, long lastupdate, int voltage, String status) {
        this.doorstatus = doorstatus;
        this.odometer = odometer;
        this.motion = motion;
        this.protectionmode = protectionmode;
        this.temperature = temperature;
        this.ismaster = ismaster;
        this.enginstatus = enginstatus;
        this.lastupdate = lastupdate;
        this.voltage = voltage;
        this.status = status;
    }

    public int getDoorstatus() {
        return doorstatus;
    }

    public void setDoorstatus(int doorstatus) {
        this.doorstatus = doorstatus;
    }

    public float getOdometer() {
        return odometer;
    }

    public void setOdometer(float odometer) {
        this.odometer = odometer;
    }

    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    public boolean isProtectionmode() {
        return protectionmode;
    }

    public void setProtectionmode(boolean protectionmode) {
        this.protectionmode = protectionmode;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public boolean ismaster() {
        return ismaster;
    }

    public void setIsmaster(boolean ismaster) {
        this.ismaster = ismaster;
    }

    public int getEnginstatus() {
        return enginstatus;
    }

    public void setEnginstatus(int enginstatus) {
        this.enginstatus = enginstatus;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
