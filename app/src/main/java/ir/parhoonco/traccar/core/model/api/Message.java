package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by mao on 9/6/2016.
 */
public class Message extends SugarRecord{
    String senderid;
    String receiverid;
    @Unique
    int messageid;
    long time;
    String body;
    String status;
    Ticket ticket;

    public Message(){}

    public Message(String senderid, String receiverid, int messageid, long time, String body, String status) {
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.messageid = messageid;
        this.time = time;
        this.body = body;
        this.status = status;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
