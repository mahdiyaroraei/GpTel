package ir.parhoonco.traccar.core.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by mao on 8/13/2016.
 */
public class PaymentItem {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String desc;

    @DatabaseField
    private String price;

    public PaymentItem(String title, String desc, String price) {
        this.title = title;
        this.desc = desc;
        this.price = price;
    }

    public PaymentItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
