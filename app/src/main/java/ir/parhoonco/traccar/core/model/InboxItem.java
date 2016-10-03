package ir.parhoonco.traccar.core.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by mao on 8/13/2016.
 */
public class InboxItem {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String desc;

    public InboxItem(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public InboxItem() {
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
