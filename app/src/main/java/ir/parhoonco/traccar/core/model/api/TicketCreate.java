package ir.parhoonco.traccar.core.model.api;

/**
 * Created by mao on 9/6/2016.
 */
public class TicketCreate {
    private int messageid;
    private long time;
    private int ticketid;

    public TicketCreate(int messageid, long time, int ticketid) {
        this.messageid = messageid;
        this.time = time;
        this.ticketid = ticketid;
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

    public int getTicketid() {
        return ticketid;
    }

    public void setTicketid(int ticketid) {
        this.ticketid = ticketid;
    }
}
