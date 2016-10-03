package ir.parhoonco.traccar.core.model.api;

/**
 * Created by mao on 9/6/2016.
 */
public class MessageCreate {
    private int messageid;
    private long time;

    public MessageCreate(int messageid, long time) {
        this.messageid = messageid;
        this.time = time;
    }

    public int getMessageid() {
        return messageid;
    }

    public void setMessageid(int messageid) {
        this.messageid = messageid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
