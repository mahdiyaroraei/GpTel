package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by mao on 9/21/2016.
 */
public class Command extends SugarRecord {
    String attributes;
    long time;
    String type;
    int commandid;
    String status;
    Device device;

    public Command(){}

    public Command(String attributes, long time, String type, int commandid, String status) {
        this.attributes = attributes;
        this.time = time;
        this.type = type;
        this.commandid = commandid;
        this.status = status;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
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

    public int getCommandid() {
        return commandid;
    }

    public void setCommandid(int commandid) {
        this.commandid = commandid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
