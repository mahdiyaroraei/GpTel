package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/4/2016.
 */
public class User extends SugarRecord{
    String phonenumber;
    String name;
    boolean ismaster;

    public User(String phonenumber, String name, boolean ismaster) {
        this.phonenumber = phonenumber;
        this.name = name;
        this.ismaster = ismaster;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean ismaster() {
        return ismaster;
    }

    public void setIsmaster(boolean ismaster) {
        this.ismaster = ismaster;
    }
}
