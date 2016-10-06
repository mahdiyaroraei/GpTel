package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/4/2016.
 */
public class User extends SugarRecord{
    String userid;
    String name;
    boolean ismaster;

    public User(String userid, String name, boolean ismaster) {
        this.userid = userid;
        this.name = name;
        this.ismaster = ismaster;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
