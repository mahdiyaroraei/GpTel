package ir.parhoonco.traccar.core;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import ir.parhoonco.traccar.ui.fragment.MapFragment;

/**
 * Created by Parhoon on 7/24/2016.
 */
public class LocationController implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static LocationController instance;
    private LocationManager locationManager;
    private Location location;
    private double latitude;
    private double longitude;
    private boolean canGetLocation = false;
    private LocationInterface locationInterface;

    public LocationController(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocationController getInstance(Context context) {
        if (instance == null) {
            instance = new LocationController(context);
        }
        return instance;
    }

    public void requestLocation(LocationInterface locationInterface) {
        this.locationInterface = locationInterface;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);

        try {
            locationManager.requestSingleUpdate(provider, this, Looper.myLooper());
        } catch (SecurityException e) {
        }
    }

    public interface LocationInterface{
        void onUpdate(Location location);
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(LocationController.this);
            } catch (SecurityException e) {
            }
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        locationInterface.onUpdate(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
