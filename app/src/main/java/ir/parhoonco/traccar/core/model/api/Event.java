package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/14/2016.
 */
public class Event extends SugarRecord{
    long positionid;
    long time;
    String type;
    Device device;

    public Event(){}

    public Event(long positionid, long time, String type) {
        this.positionid = positionid;
        this.time = time;
        this.type = type;
    }

    public long getPositionid() {
        return positionid;
    }

    public void setPositionid(long positionid) {
        this.positionid = positionid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
