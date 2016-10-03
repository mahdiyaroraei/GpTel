package ir.parhoonco.traccar.core.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by mao on 8/13/2016.
 */
public class TicketItem {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String desc;

    public TicketItem(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public TicketItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
