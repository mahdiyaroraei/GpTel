package ir.parhoonco.traccar.core.model.api;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mao on 9/4/2016.
 */
public class Device extends SugarRecord {
    @Ignore
    private OnDevicePositionSetListener positionSetListener;
    String name;
    @Unique
    String imei;
    String masterid;
    int positionid;
    String phonenumber;
    long creationtime;
    long modificationtime;
    String model;
    long lastupdate;
    String status;
    int odemeter;
    String motion;
    String selltype;
    boolean enable;
    @Ignore
    ArrayList<User> users;
    Credit credit;
    Position position;
    User user;
    Insurance insurance;
    DeviceConfig deviceConfig;
    DeviceInfo deviceInfo;

    public Device() {
    }

    public Device(String name, String imei) {
        this.name = name;
        this.imei = imei;
    }

    public Device(String name, String imei, String masterid, int positionid, String phonenumber, long creationtime, long modificationtime, String model, long lastupdate, String status, int odemeter, String motion, String selltype, boolean enable, ArrayList<User> users) {
        this.name = name;
        this.imei = imei;
        this.masterid = masterid;
        this.positionid = positionid;
        this.phonenumber = phonenumber;
        this.creationtime = creationtime;
        this.modificationtime = modificationtime;
        this.model = model;
        this.lastupdate = lastupdate;
        this.status = status;
        this.odemeter = odemeter;
        this.motion = motion;
        this.selltype = selltype;
        this.enable = enable;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMasterid() {
        return masterid;
    }

    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }

    public int getPositionid() {
        return positionid;
    }

    public void setPositionid(int positionid) {
        this.positionid = positionid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public long getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(long creationtime) {
        this.creationtime = creationtime;
    }

    public long getModificationtime() {
        return modificationtime;
    }

    public void setModificationtime(long modificationtime) {
        this.modificationtime = modificationtime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOdemeter() {
        return odemeter;
    }

    public void setOdemeter(int odemeter) {
        this.odemeter = odemeter;
    }

    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    public String getSelltype() {
        return selltype;
    }

    public void setSelltype(String selltype) {
        this.selltype = selltype;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        if (positionSetListener != null) {
            positionSetListener.onSet(position);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    public void setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public List<PmConfig> getPmConfigs() {
        return PmConfig.find(PmConfig.class, "device = ?", String.valueOf(getId()));
    }

    public List<Position> getPositions() {
        return Position.find(Position.class, "device = ?", String.valueOf(getId()));
    }

    public List<Payment> getPayments() {
        return Payment.find(Payment.class, "device = ?", String.valueOf(getId()));
    }

    public List<Pm> getPms() {
        return Pm.find(Pm.class, "device = ?", new String[]{String.valueOf(getId())}, null, "time", null);
    }

    public List<Event> getEvents() {
        return Event.find(Event.class, "device = ?", new String[]{String.valueOf(getId())}, null, "time DESC", "100");
    }

    public List<Command> getCommands() {
        return Command.find(Command.class, "device = ?", new String[]{String.valueOf(getId())}, null, "time DESC", "100");
    }

    public void setPositionListener(OnDevicePositionSetListener positionSetListener) {
        this.positionSetListener = positionSetListener;
    }

    public interface OnDevicePositionSetListener {
        void onSet(Position position);
    }
}
