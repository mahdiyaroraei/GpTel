package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/11/2016.
 */
public class Pm extends SugarRecord{
    float odometer;
    String imei;
    long time;
    String userid;
    String pmkey;
    Device device;

    public Pm(){}

    public Pm(float odometer, String imei, long time, String userid, String pmkey) {
        this.odometer = odometer;
        this.imei = imei;
        this.time = time;
        this.userid = userid;
        this.pmkey = pmkey;
    }

    public float getOdometer() {
        return odometer;
    }

    public void setOdometer(float odometer) {
        this.odometer = odometer;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPmkey() {
        return pmkey;
    }

    public void setPmkey(String pmkey) {
        this.pmkey = pmkey;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
