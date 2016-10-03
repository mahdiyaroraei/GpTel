package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/19/2016.
 */
public class DeviceConfig extends SugarRecord {
    String imei;
    String minvoltage;
    String minangle;
    String minperiod;
    String sendperiod;
    String mindistance;
    boolean protectionmode;
    boolean autooff;
    boolean autogeofence;
    int maxspeed;
    String status;

    public DeviceConfig() {
    }


    public DeviceConfig(String minvoltage, String minangle, String minperiod, String sendperiod, String mindistance, boolean protectionmode, boolean autooff, boolean autogeofence, int maxspeed) {
        this.minvoltage = minvoltage;
        this.minangle = minangle;
        this.minperiod = minperiod;
        this.sendperiod = sendperiod;
        this.mindistance = mindistance;
        this.protectionmode = protectionmode;
        this.autooff = autooff;
        this.autogeofence = autogeofence;
        this.maxspeed = maxspeed;
    }

    public String getMinvoltage() {
        return minvoltage;
    }

    public void setMinvoltage(String minvoltage) {
        this.minvoltage = minvoltage;
    }

    public String getMinangle() {
        return minangle;
    }

    public void setMinangle(String minangle) {
        this.minangle = minangle;
    }

    public String getMinperiod() {
        return minperiod;
    }

    public void setMinperiod(String minperiod) {
        this.minperiod = minperiod;
    }

    public String getSendperiod() {
        return sendperiod;
    }

    public void setSendperiod(String sendperiod) {
        this.sendperiod = sendperiod;
    }

    public String getMindistance() {
        return mindistance;
    }

    public void setMindistance(String mindistance) {
        this.mindistance = mindistance;
    }

    public boolean isProtectionmode() {
        return protectionmode;
    }

    public void setProtectionmode(boolean protectionmode) {
        this.protectionmode = protectionmode;
    }

    public boolean isAutooff() {
        return autooff;
    }

    public void setAutooff(boolean autooff) {
        this.autooff = autooff;
    }

    public boolean isAutogeofence() {
        return autogeofence;
    }

    public void setAutogeofence(boolean autogeofence) {
        this.autogeofence = autogeofence;
    }

    public int getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(int maxspeed) {
        this.maxspeed = maxspeed;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
