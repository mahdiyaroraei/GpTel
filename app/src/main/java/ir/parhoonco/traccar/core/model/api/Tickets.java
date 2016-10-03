package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

import java.util.List;

import ir.parhoonco.traccar.core.model.api.Ticket;

/**
 * Created by mao on 9/6/2016.
 */
public class Tickets extends SugarRecord{
    private int ticketscount;
    private List<Ticket> tickets;

    public Tickets(int ticketscount, List<Ticket> tickets) {
        this.ticketscount = ticketscount;
        this.tickets = tickets;
    }

    public int getTicketscount() {
        return ticketscount;
    }

    public void setTicketscount(int ticketscount) {
        this.ticketscount = ticketscount;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
