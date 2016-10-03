package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/14/2016.
 */
public class Payment extends SugarRecord{
    String paytoken;
    long price;
    long duetime;
    long paytime;
    String paymenturl;
    Device device;

    public Payment(){}

    public Payment(String paytoken, long price, long duetime, long paytime, String paymenturl) {
        this.paytoken = paytoken;
        this.price = price;
        this.duetime = duetime;
        this.paytime = paytime;
        this.paymenturl = paymenturl;
    }

    public String getPaytoken() {
        return paytoken;
    }

    public void setPaytoken(String paytoken) {
        this.paytoken = paytoken;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getDuetime() {
        return duetime;
    }

    public void setDuetime(long duetime) {
        this.duetime = duetime;
    }

    public long getPaytime() {
        return paytime;
    }

    public void setPaytime(long paytime) {
        this.paytime = paytime;
    }

    public String getPaymenturl() {
        return paymenturl;
    }

    public void setPaymenturl(String paymenturl) {
        this.paymenturl = paymenturl;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
