package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by mao on 9/5/2016.
 */
public class PmConfig extends SugarRecord{
    int distancethreshold;
    int startodometer;
    int timethreshold;
    long starttime;
    String pmkey;
    Device device;

    public PmConfig(){}

    public PmConfig(int distancethreshold, int startodometer, int timethreshold, long starttime, String pmkey) {
        this.distancethreshold = distancethreshold;
        this.startodometer = startodometer;
        this.timethreshold = timethreshold;
        this.starttime = starttime;
        this.pmkey = pmkey;
    }

    public int getDistancethreshold() {
        return distancethreshold;
    }

    public void setDistancethreshold(int distancethreshold) {
        this.distancethreshold = distancethreshold;
    }

    public int getStartodometer() {
        return startodometer;
    }

    public void setStartodometer(int startodometer) {
        this.startodometer = startodometer;
    }

    public int getTimethreshold() {
        return timethreshold;
    }

    public void setTimethreshold(int timethreshold) {
        this.timethreshold = timethreshold;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
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
