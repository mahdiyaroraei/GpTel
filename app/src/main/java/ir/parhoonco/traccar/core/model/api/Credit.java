package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/15/2016.
 */
public class Credit extends SugarRecord{
    long amount;
    long time;

    public Credit(){}

    public Credit(long amount, long time) {
        this.amount = amount;
        this.time = time;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
