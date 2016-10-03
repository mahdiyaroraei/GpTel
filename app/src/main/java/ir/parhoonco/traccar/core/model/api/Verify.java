package ir.parhoonco.traccar.core.model.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mao on 9/4/2016.
 */
public class Verify {
    private String apikey;
    private ArrayList<Device> devices;
    private String name;
    private double lat;
    private double lon;
    private double zoom;

    public Verify(String apikey, ArrayList<Device> devices, String name, double lat, double lon, double zoom) {
        this.apikey = apikey;
        this.devices = devices;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.zoom = zoom;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
