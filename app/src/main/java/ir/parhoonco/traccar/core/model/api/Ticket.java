package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by mao on 9/6/2016.
 */
public class Ticket extends SugarRecord{
    String senderid;
    String receiverid;
    String subject;
    long lastupdate;
    long time;
    String body;
    @Unique
    int ticketid;
    String status;

    public Ticket(){}

    public Ticket(String senderid, String receiverid, String subject, long lastupdate, long time, String body, int ticketid, String status) {
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.subject = subject;
        this.lastupdate = lastupdate;
        this.time = time;
        this.body = body;
        this.ticketid = ticketid;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
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

    public int getTicketid() {
        return ticketid;
    }

    public void setTicketid(int ticketid) {
        this.ticketid = ticketid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Message> getMessages(){
        return Message.find(Message.class , "ticket = ?" ,  new String[]{String.valueOf(getId())} , null , "time DESC" , null);
    }
}
