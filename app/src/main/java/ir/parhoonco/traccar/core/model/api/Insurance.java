package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;

/**
 * Created by mao on 9/14/2016.
 */
public class Insurance extends SugarRecord{
    String firstname;
    String lastname;
    String nationalcode;
    String address;
    long creationtime;
    String userid;

    public Insurance(){}

    public Insurance(String firstname, String lastname, String nationalcode, String address, long creationtime, String userid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.nationalcode = nationalcode;
        this.address = address;
        this.creationtime = creationtime;
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNationalcode() {
        return nationalcode;
    }

    public void setNationalcode(String nationalcode) {
        this.nationalcode = nationalcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(long creationtime) {
        this.creationtime = creationtime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
