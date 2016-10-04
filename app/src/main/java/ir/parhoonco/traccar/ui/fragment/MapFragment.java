package ir.parhoonco.traccar.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.LocationController;
import ir.parhoonco.traccar.core.SocketController;
import ir.parhoonco.traccar.core.TeltonikaSmsProtocol;
import ir.parhoonco.traccar.core.model.api.Address;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.Position;
import ir.parhoonco.traccar.core.model.api.Positions;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.LaunchActivity;
import ir.parhoonco.traccar.ui.component.AndroidMarker;
import ir.parhoonco.traccar.ui.dialog.SuccessDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Parhoon on 7/26/2016.
 */
public class MapFragment extends Fragment implements DatePickerDialog.OnDateSetListener, Callback<Position>, TimePickerDialog.OnTimeSetListener {

    private static final String MAP_FILE = "iran.map";
    public static SuccessDialog dialog;
    public final int start_pin_drawable = R.drawable.ic_start_pin;
    public final int end_pin_drawable = R.drawable.ic_end_pin;
    public final int stop_pin_drawable = R.drawable.ic_stop_pin;
    public final int other_pin_drawable = R.drawable.ic_other_pin;
    private final String FROM_DATEPICKER = "fromDatePicker";
    private final String TO_DATEPICKER = "toDatePicker";
    private boolean isFrom = true;
    private MapView mapView;
    private TextView fromTextView;
    private TextView toTextView;
    private long startTime;
    private long endTime;
    private Position devicePosition;
    private boolean isFromSet = false;
    private boolean isToSet = false;
    private boolean isPopupShow = false;
    private View popupLayout;
    private ViewGroup view;
    private ArrayList<Layer> markers = new ArrayList<>();
    private ThinDownloadManager downloadManager;
    private AndroidMarker.OnMarkerTap onMarkerTapInstance;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((LaunchActivity) getActivity()).onTabClicked(R.id.locationTab);
        ApplicationLoader.isInternetAvailable();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.fragment_map, null);

        LocationController.getInstance(getContext());

        mapInit(view, inflater);

        initMapHistory(view);

        initDistance(view);


        return view;
    }

    public void mapInit(View view, final LayoutInflater inflater) {
        this.mapView = new MapView(getContext());

        this.mapView.setClickable(true);
        this.mapView.getMapScaleBar().setVisible(true);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setZoomLevelMin((byte) 10);
        this.mapView.setZoomLevelMax((byte) 20);


        // create a tile cache of suitable size
        TileCache tileCache = AndroidUtil.createTileCache(getContext(), "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                this.mapView.getModel().frameBufferModel.getOverdrawFactor());

        // tile renderer layer using internal render theme
        if (!new File(Environment.getExternalStorageDirectory(), MAP_FILE).exists()) {
            final AppCompatDialog dialog_download = new AppCompatDialog(getContext());
            dialog_download.setCancelable(true);
            dialog_download.setContentView(inflater.inflate(R.layout.dialog_map_download, null));
            dialog_download.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_download.dismiss();
                    final AppCompatDialog dialog = new AppCompatDialog(getContext());
                    dialog.setCancelable(false);
                    dialog.addContentView(inflater.inflate(R.layout.download_layout, null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    dialog.show();

                    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Jersey.ttf");
                    ((TextView) dialog.findViewById(R.id.progress_txt)).setTypeface(typeface);
                    ((TextView) dialog.findViewById(R.id.file_size_txt)).setTypeface(typeface);

                    Uri downloadUri = Uri.parse("http://download.mapsforge.org/maps/asia/iran.map");
                    Uri destinationUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + MAP_FILE);
                    DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                            .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                            .setDownloadListener(new DownloadStatusListener() {
                                @Override
                                public void onDownloadComplete(int id) {
                                    dialog.dismiss();
                                    FragmentHelper.getInstance(getContext()).addToStack(new MapFragment());
                                }

                                @Override
                                public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                                    dialog.dismiss();
                                    FragmentHelper.getInstance(getContext()).addToStack(new CarFragment());
                                }

                                @Override
                                public void onProgress(int id, long totalBytes, int progress) {
                                    ((TextView) dialog.findViewById(R.id.progress_txt)).setText(progress + " MB");
                                    ((ContentLoadingProgressBar) dialog.findViewById(R.id.progress_wheel)).setProgress(progress);
                                }
                            });
                    downloadManager = new ThinDownloadManager();
                    int downloadId = downloadManager.add(downloadRequest);

                    dialog.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadManager.cancelAll();
                            dialog.dismiss();
                            FragmentHelper.getInstance(getContext()).addToStack(new CarFragment());
                        }
                    });
                }
            });
            dialog_download.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_download.dismiss();
                    FragmentHelper.getInstance(getContext()).addToStack(new CarFragment());
                }
            });
            dialog_download.show();
        } else {
            checkInternetConnection();
            MapDataStore mapDataStore = new MapFile(new File(Environment.getExternalStorageDirectory(), MAP_FILE));
            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    this.mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

            // only once a layer is associated with a mapView the rendering starts
            this.mapView.getLayerManager().getLayers().add(tileRendererLayer);

            CarFragment.defaultDevice.setPositionListener(new Device.OnDevicePositionSetListener() {
                @Override
                public void onSet(Position position) {
                    devicePosition = position;
                    removeMarkers();
                    addMarker(devicePosition, other_pin_drawable);
                    mapView.setCenter(new LatLong(devicePosition.getLat(), devicePosition.getLon()));
                    mapView.setZoomLevel((byte) 15);
                }
            });

            Call<Position> call = ApplicationLoader.api.getLastPosition(CarFragment.defaultImei);
            call.enqueue(this);

            this.mapView.setZoomLevel((byte) 15);
            view.findViewById(R.id.power).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeMarkers();
                    addMarker(devicePosition, other_pin_drawable);
                    mapView.setCenter(new LatLong(devicePosition.getLat(), devicePosition.getLon()));
                    mapView.setZoomLevel((byte) 15);
                }
            });
            ((RelativeLayout) view.findViewById(R.id.mapLayout)).addView(mapView);
        }
    }

    private void checkInternetConnection() {
        if (!ApplicationLoader.isInternetAccess) {
            TextView titleTextView = new TextView(getContext());
            titleTextView.setGravity(Gravity.RIGHT);
            titleTextView.setTextColor(Color.BLACK);
            titleTextView.setText(R.string.internet_dialog_title);
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setCustomTitle(titleTextView)
                    .setMessage(R.string.internet_dialog_message)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                TeltonikaSmsProtocol.sendCommand(CarFragment.defaultDevice, TeltonikaSmsProtocol.TYPE_GETGPS, null, getContext());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*final View statusLayout = view.findViewById(R.id.statusLayout);
                            final TextView statusTextView = (TextView) view.findViewById(R.id.statusTextView);

                            statusLayout.setVisibility(View.VISIBLE);
                            statusTextView.setText(R.string.sms_command_sending);

                            Object object = new SMSController.NotificationCenterDelegate() {
                                @Override
                                public void didReceivedNotification(int id, Object... params) {
                                    int status = 0;
                                    switch (id) {
                                        case SMSController.didDeliveredSMS:
                                            status = R.string.sms_command_delivered;
                                            break;
                                        case SMSController.didSentSMS:
                                            status = R.string.sms_command_sent;
                                            break;
                                        case SMSController.didReceivedNewSMS:
                                            status = R.string.sms_command_receive;
                                            statusLayout.setVisibility(View.INVISIBLE);
//                                            ((TextView)view.findViewById(R.id.speedTextView)).setText(SMSController.speed + " Km/h");
                                            break;
                                    }
                                    statusTextView.setText(status);
                                }
                            };
                            SMSController.getInstance(getContext()).addObserver(object, SMSController.didSentSMS);
                            SMSController.getInstance(getContext()).addObserver(object, SMSController.didDeliveredSMS);
                            SMSController.getInstance(getContext()).addObserver(object, SMSController.didReceivedNewSMS);*/
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
    }

    private void initMapHistory(final View view) {
        view.findViewById(R.id.mapHistoryTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMarkers();
                if (isPopupShow == false) {
                    isPopupShow = true;
                    popupLayout = ((LayoutInflater) getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.popup_map_history_layout, null);

                    fromTextView = (TextView) popupLayout.findViewById(R.id.fromDateTextView);
                    toTextView = (TextView) popupLayout.findViewById(R.id.toDateTextView);

                    Button button = (Button) popupLayout.findViewById(R.id.submit_btn);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showHistory();
                        }
                    });

                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int height = size.y;

                    ((RelativeLayout) view.findViewById(R.id.mapLayout)).addView(popupLayout, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    popupLayout.findViewById(R.id.remove_img).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((RelativeLayout) view.findViewById(R.id.mapLayout)).removeView(popupLayout);
                            isPopupShow = false;
                        }
                    });

                    popupLayout.findViewById(R.id.fromDateTextView).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PersianCalendar persianCalendar = new PersianCalendar();
                            persianCalendar.setTimeZone(TimeZone.getDefault());
                            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                                    MapFragment.this,
                                    persianCalendar.getPersianYear(),
                                    persianCalendar.getPersianMonth(),
                                    persianCalendar.getPersianDay()
                            );
                            datePickerDialog.show(getActivity().getFragmentManager(), FROM_DATEPICKER);
                        }
                    });

                    popupLayout.findViewById(R.id.toDateTextView).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PersianCalendar persianCalendar = new PersianCalendar();
                            persianCalendar.setTimeZone(TimeZone.getDefault());
                            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                                    MapFragment.this,
                                    persianCalendar.getPersianYear(),
                                    persianCalendar.getPersianMonth(),
                                    persianCalendar.getPersianDay()
                            );
                            datePickerDialog.show(getActivity().getFragmentManager(), TO_DATEPICKER);
                        }
                    });
                }
            }
        });
    }

    private void initDistance(final View view) {
        view.findViewById(R.id.distanceTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMarkers();
                dialog = new SuccessDialog();
                dialog.showDialog(getActivity(), "لطفا تا دریافت موقعیت مکانی گوشی منتظر بمانید...");
                dialog.dialogButton.setVisibility(View.INVISIBLE);
                LocationController.getInstance(getContext()).requestLocation(new LocationController.LocationInterface() {
                    @Override
                    public void onUpdate(Location location) {
                        try {
                            dialog.dimmisDialog();
                            TextView distanceTextView = new TextView(getContext(), null);
                            distanceTextView.setTextColor(Color.GREEN);
                            distanceTextView.setBackgroundResource(R.drawable.bg_shadow);
                            float dist = showDistance(location);
                            if (dist != 0.0) {
                                distanceTextView.setText(dist + "متر");
                                ((LinearLayout) view).addView(distanceTextView);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    private void showHistory() {
        ((ViewGroup) popupLayout.getParent()).removeView(popupLayout);

        Call<Positions> positionsCall = ApplicationLoader.api.getPositions(CarFragment.defaultImei, startTime / 1000, endTime / 1000, 100, 0);
//        Call<List<Position>> positionsCall = ApplicationLoader.api.getPositions(CarFragment.defaultImei, 1474159507, 1474194906);
        positionsCall.enqueue(new Callback<Positions>() {
            @Override
            public void onResponse(Call<Positions> call, Response<Positions> response) {
                try {
                    if (response.code() == 200 && response.body().getPositions().size() > 0) {

                        final List<Position> latLongs = response.body().getPositions();

                        for (int i = 0; i < latLongs.size(); i++) {
                            latLongs.get(i).setDevice(CarFragment.defaultDevice);
                            latLongs.get(i).save();
                            int drawable;
                            if (i == 0) {
                                drawable = start_pin_drawable;
                            } else if (i == latLongs.size() - 1) {
                                drawable = end_pin_drawable;
                            } else {
                                drawable = other_pin_drawable;
                            }
                            addMarker(latLongs.get(i), drawable);
                        }

                        Dimension mapViewDimension = mapView.getModel().mapViewDimension.getDimension();
                        int tileSize = mapView.getModel().displayModel.getTileSize();
                        double minLat = Math.min(latLongs.get(latLongs.size() - 1).getLat(), latLongs.get(0).getLat());
                        double minLon = Math.min(latLongs.get(latLongs.size() - 1).getLon(), latLongs.get(0).getLon());
                        double maxLat = Math.max(latLongs.get(latLongs.size() - 1).getLat(), latLongs.get(0).getLat());
                        double maxLon = Math.max(latLongs.get(latLongs.size() - 1).getLon(), latLongs.get(0).getLon());
                        mapView.setZoomLevel(LatLongUtils.zoomForBounds(mapViewDimension, new BoundingBox(minLat, minLon, maxLat, maxLon), tileSize));
                        if (latLongs.size() > 1) {
                            final Thread thread = new
                                    Thread(new Runnable() {
                                @Override
                                public void run() {
                                    boolean isEnded = false;
                                    boolean isPoint = false;
                                    int counter = 0;
                                    Marker marker = null;
                                    try {
                                        marker = addNav(new LatLong(latLongs.get(counter).getLat(), latLongs.get(counter).getLon()),
                                                angleFromCoordinate(latLongs.get(counter).getLat(), latLongs.get(counter).getLon(), latLongs.get(counter + 1).getLat(), latLongs.get(counter + 1).getLon()));
                                    } catch (Exception e) {
                                        return;
                                    }
                                    while (!isEnded) {
                                        try {
                                            for (LatLong latLong :
                                                    divide(new LatLong(latLongs.get(counter).getLat(), latLongs.get(counter).getLon())
                                                            , new LatLong(latLongs.get(counter + 1).getLat(), latLongs.get(counter + 1).getLon()), 6)) {
                                                marker.setLatLong(latLong);
                                                rotateMarker(marker, angleFromCoordinate(latLongs.get(counter).getLat(), latLongs.get(counter).getLon(),
                                                        latLongs.get(counter + 1).getLat(), latLongs.get(counter + 1).getLon()));
                                                marker.requestRedraw();
                                                Thread.sleep(750);
                                            }
                                            counter++;
                                        } catch (IndexOutOfBoundsException e) {
                                            isEnded = true;
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            return;
                                        }
                                    }
                                }
                            });
                            thread.start();
                        }

                        mapView.setCenter(new LatLong(latLongs.get(0).getLat(), latLongs.get(0).getLon()));


                        drawPolyline(latLongs);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Positions> call, Throwable t) {
                String sad = "";
            }
        });
        SocketController.getInstance().getDevicePositionHistory(CarFragment.defaultImei, startTime, endTime, new SocketController.CallBackResponse() {
            @Override
            public void didReceiveResponse(boolean success, Map<String, Object> params) {

            }
        });
    }

    private double bearing(double lat1, double lng1, double lat2, double lng2) {
        double dLon = lng2 - lng1;
        double cos_2 = Math.cos(lat2);
        double y = Math.sin(dLon) * cos_2;
        double x = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * cos_2 * Math.cos(dLon);
        double theta = Math.atan2(y, x);
        return theta;
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {
        int R = 6371000; // meters
        double dLat = lat2 - lat1;
        double dLon = lng2 - lng1;
        double sinLat = Math.sin(dLat / 2);
        double sinLng = Math.sin(dLon / 2);
        double a = sinLat * sinLat +
                sinLng * sinLng * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }

    private LatLong offset(double lat1, double lng1, double bearing, double distance) {
        int R = 6371000; // meters
        double d = distance / R;
        double cos_d = Math.cos(d);
        double sin_d = Math.sin(d);
        double sin_1 = Math.sin(lat1);
        double cos_1 = Math.cos(lat1);
        double lat2 = Math.asin(sin_1 * cos_d + cos_1 * sin_d * Math.cos(bearing));
        double sin_2 = Math.sin(lat2);
        double lng2 = lng1 +
                Math.atan2(Math.sin(bearing) * sin_d * cos_1, cos_d - sin_1 * sin_2);
        return new LatLong(lat2, lng2);
    }

    private ArrayList<LatLong> divide(LatLong p1, LatLong p2, int n) {
        ArrayList<LatLong> latLongs = new ArrayList<>();
        double lat1 = p1.getLatitude() * Math.PI / 180;
        double lng1 = p1.getLongitude() * Math.PI / 180;
        double lat2 = p2.getLatitude() * Math.PI / 180;
        double lng2 = p2.getLongitude() * Math.PI / 180;
        double heading = bearing(lat1, lng1, lat2, lng2);
        double D = distance(lat1, lng1, lat2, lng2);

        for (int i = 1; i < n; ++i) {
            LatLong p = offset(lat1, lng1, heading, i * D / n);

            double lat = p.getLatitude() * 180 / Math.PI;
            double lng = p.getLongitude() * 180 / Math.PI;
            LatLong latLng = new LatLong(lat, lng);
            latLongs.add(latLng);
        }
        return latLongs;
    }

    private float showDistance(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (latitude != 0.0 && longitude != 0.0) {
//            ArrayList<LatLong> latLongs = new ArrayList<>();
            LatLong myPosition = new LatLong(latitude, longitude);
//            latLongs.add(myPosition);
//            latLongs.add(devicePosition);

            try {
                addNav(myPosition, angleFromCoordinate(myPosition.getLatitude(), myPosition.getLongitude(), devicePosition.getLat(), devicePosition.getLon()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            addMarker(devicePosition, other_pin_drawable);

            Dimension mapViewDimension = mapView.getModel().mapViewDimension.getDimension();
            int tileSize = mapView.getModel().displayModel.getTileSize();

            double minLat = Math.min(myPosition.getLatitude(), devicePosition.getLat());
            double minLon = Math.min(myPosition.getLongitude(), devicePosition.getLon());
            double maxLat = Math.max(myPosition.getLatitude(), devicePosition.getLat());
            double maxLon = Math.max(myPosition.getLongitude(), devicePosition.getLon());
            mapView.setZoomLevel(LatLongUtils.zoomForBounds(mapViewDimension, new BoundingBox(minLat, minLon, maxLat, maxLon), tileSize));

//            drawPolylineWithLatLong(latLongs);

            Location loc1 = new Location("");
            loc1.setLatitude(latitude);
            loc1.setLongitude(longitude);

            Location loc2 = new Location("");
            loc2.setLatitude(devicePosition.getLat());
            loc2.setLongitude(devicePosition.getLon());

            return loc1.distanceTo(loc2);
        }
        return 0;
    }

    private void addMarker(final Position position, int res) {
        try {
            if (position.getSpeed() == 0) {
                res = stop_pin_drawable;
            }
            final AndroidMarker marker = new AndroidMarker(position, AndroidGraphicFactory.convertToBitmap(
                    getResources().getDrawable(R.drawable.ic_oval)), 0, 0);
            final int finalRes = res;
            AndroidMarker.OnMarkerTap onMarkerTap = new AndroidMarker.OnMarkerTap() {
                @Override
                public void onTap() {
                    if (!marker.isBubbleVisible()) {
                        marker.setBitmap(AndroidGraphicFactory.convertToBitmap(
                                getResources().getDrawable(finalRes)));
                        marker.requestRedraw();
                        marker.setOnTap(new AndroidMarker.OnMarkerTap() {
                            @Override
                            public void onTap() {
                                final View bubbleView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.bubble_layout, null);
                                TextView timeTextView = (TextView) bubbleView.findViewById(R.id.time_txt);
                                TextView speedTextView = (TextView) bubbleView.findViewById(R.id.speed_txt);
                                TextView positionTextView = (TextView) bubbleView.findViewById(R.id.lat_lon_txt);
                                final TextView addressTextView = (TextView) bubbleView.findViewById(R.id.address_txt);

                                PersianCalendar calendar = new PersianCalendar(position.getFixtime() * 1000);
                                calendar.setTimeZone(TimeZone.getDefault());
                                timeTextView.setText(calendar.getPersianShortDateTime());
                                speedTextView.setText("سرعت: " + position.getSpeed() + " کیلومتر بر ساعت");
                                positionTextView.setText("طول و عرض جغرافیایی: " + position.getLat() + " و " + position.getLon());
                                if (position.getAddress() != null) {
                                    addressTextView.setText(position.getAddress());
                                } else {
                                    String udata = "آدرس";
                                    SpannableString content = new SpannableString(udata);
                                    content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
                                    addressTextView.setTextColor(Color.parseColor("#47ABD2"));
                                    addressTextView.setText(content);
                                    addressTextView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Call<Address> addressCall = ApplicationLoader.api.getAddress(String.valueOf(position.getPositionid()));
                                            addressCall.enqueue(new Callback<Address>() {
                                                @Override
                                                public void onResponse(Call<Address> call, Response<Address> response) {
                                                    if (response.code() == 200) {
                                                        addressTextView.setTextColor(Color.WHITE);
                                                        addressTextView.setText(response.body().getAddress());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Address> call, Throwable t) {
                                                    String asd = "";
                                                }
                                            });
                                        }
                                    });
                                }
                                MapView.LayoutParams layoutParams = new MapView.LayoutParams(ir.parhoonco.traccar.core.AndroidUtil.dpToPx(325, getResources()), ir.parhoonco.traccar.core.AndroidUtil.dpToPx(200, getResources()), new LatLong(position.getLat(), position.getLon()), MapView.LayoutParams.Alignment.BOTTOM_CENTER);
                                mapView.addView(bubbleView, layoutParams);
                                marker.setBubbleVisible(true);
                                bubbleView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ((ViewGroup) bubbleView.getParent()).removeView(bubbleView);
                                        marker.setBubbleVisible(false);
                                        marker.setBitmap(AndroidGraphicFactory.convertToBitmap(
                                                getResources().getDrawable(R.drawable.ic_oval)));
                                        marker.setOnTap(onMarkerTapInstance);
                                        marker.requestRedraw();
                                    }
                                });
                            }
                        });
                    }
                }
            };
            onMarkerTapInstance = onMarkerTap;
            marker.setOnTap(onMarkerTap);

            markers.add(marker);

            this.mapView.getLayerManager().getLayers().add(marker);
        } catch (Exception e) {
        }
    }


    private Marker addNav(LatLong latLong, double angle) throws Exception {
        Marker marker = new Marker(latLong, AndroidGraphicFactory.convertToBitmap(rotateDrawable(R.drawable.ic_nav, angle)), 0, 0);

        markers.add(marker);

        this.mapView.getLayerManager().getLayers().add(marker);

        return marker;
    }

    private void rotateMarker(Marker marker, double angle) throws Exception {
        marker.setBitmap(AndroidGraphicFactory.convertToBitmap(rotateDrawable(R.drawable.ic_nav, angle)));
    }

    private double angleFromCoordinate(double lat2, double long2, double lat1,
                                       double long1) {

        Location location = new Location("");
        location.setLatitude(lat1);
        location.setLongitude(long1);

        Location location2 = new Location("");
        location2.setLatitude(lat2);
        location2.setLongitude(long2);

        /*double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 360 - brng;

        return brng;*/

        return location2.bearingTo(location);
    }

    private BitmapDrawable rotateDrawable(@DrawableRes int resId, double angle) throws Exception {
        android.graphics.Bitmap bmpOriginal = BitmapFactory.decodeResource(getResources(), resId);
        android.graphics.Bitmap bmpResult = android.graphics.Bitmap.createBitmap(bmpOriginal.getHeight(), bmpOriginal.getWidth(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bmpResult);
        int pivot = bmpOriginal.getHeight() / 2;
        tempCanvas.rotate(Float.parseFloat(angle + ""), pivot, pivot);
        tempCanvas.drawBitmap(bmpOriginal, 0, 0, null);
        return new BitmapDrawable(getContext().getResources(), bmpResult);
    }


    private void removeMarkers() {
        for (Layer layer :
                markers) {
            this.mapView.getLayerManager().getLayers().remove(layer);
        }

        markers = new ArrayList<>();
    }

    private void drawPolylineWithLatLong(List<LatLong> latLongs) {
        Paint paintStroke = createPaint(AndroidGraphicFactory.INSTANCE
                        .createColor(org.mapsforge.core.graphics.Color.GREEN), 1,
                Style.STROKE);
        paintStroke.setDashPathEffect(new float[]{25, 15});
        paintStroke.setStrokeWidth(5);
        paintStroke.setStrokeWidth(3);
        Polyline line = new Polyline(paintStroke,
                AndroidGraphicFactory.INSTANCE);

        List<LatLong> coordinateList = line.getLatLongs();
        for (LatLong latLong :
                latLongs) {
            coordinateList.add(latLong);
        }

        markers.add(line);

        mapView.getLayerManager().getLayers().add(line);
    }

    private void drawPolyline(List<Position> latLongs) {
        Paint paintStroke = createPaint(AndroidGraphicFactory.INSTANCE
                        .createColor(org.mapsforge.core.graphics.Color.BLUE), 2,
                Style.STROKE);
//        paintStroke.setDashPathEffect(new float[]{25, 15});
        paintStroke.setStrokeWidth(5);
        paintStroke.setStrokeWidth(3);
        Polyline line = new Polyline(paintStroke,
                AndroidGraphicFactory.INSTANCE);

        List<LatLong> coordinateList = line.getLatLongs();
        for (Position position :
                latLongs) {
            coordinateList.add(new LatLong(position.getLat(), position.getLon()));
        }
        markers.add(line);

        mapView.getLayerManager().getLayers().add(line);
    }

    private Paint createPaint(int color, int strokeWidth, Style style) {
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        return paint;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        PersianCalendar calendar = new PersianCalendar();
        calendar.clear();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setPersianDate(year, monthOfYear, dayOfMonth - 1);
        if (datePickerDialog.getTag().equals(FROM_DATEPICKER)) {
            isFrom = true;
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                    MapFragment.this
                    , calendar.get(PersianCalendar.HOUR_OF_DAY)
                    , calendar.get(PersianCalendar.MINUTE)
                    , true);
            timePickerDialog.show(getActivity().getFragmentManager(), FROM_DATEPICKER);

            fromTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            startTime = calendar.getTimeInMillis();
        } else if (datePickerDialog.getTag().equals(TO_DATEPICKER)) {
            isFrom = false;
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                    MapFragment.this
                    , calendar.get(PersianCalendar.HOUR_OF_DAY)
                    , calendar.get(PersianCalendar.MINUTE)
                    , true);
            timePickerDialog.show(getActivity().getFragmentManager(), TO_DATEPICKER);
            toTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            endTime = calendar.getTimeInMillis();
        }
    }

    @Override
    public void onResponse(Call<Position> call, Response<Position> response) {
        if (response.code() == 400) {
            devicePosition = CarFragment.defaultDevice.getPosition();
        } else if (response.code() == 200) {
            devicePosition = response.body();
            devicePosition.save();
            CarFragment.defaultDevice.setPosition(devicePosition);
            CarFragment.defaultDevice.save();
        }
        if (devicePosition.getLat() != 0) {
            removeMarkers();
            mapView.setCenter(new LatLong(devicePosition.getLat(), devicePosition.getLon()));
            addMarker(devicePosition, other_pin_drawable);
        }
    }

    @Override
    public void onFailure(Call<Position> call, Throwable t) {
        devicePosition = CarFragment.defaultDevice.getPosition();
        if (devicePosition != null) {
            mapView.setCenter(new LatLong(devicePosition.getLat(), devicePosition.getLon()));
            addMarker(devicePosition, other_pin_drawable);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        if (isFrom) {
            if (!isToSet) {
                Toast.makeText(getContext(), "لطفا تاریخ پایان جست و جو را وارد نماءید.", Toast.LENGTH_LONG).show();
            } else {
                isPopupShow = false;
                isFromSet = false;
                isToSet = false;
            }
            isFromSet = true;

            fromTextView.setText(fromTextView.getText() + " " + hourOfDay + ":" + minute);
            startTime += hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000;
        } else {
            if (!isFromSet) {
                Toast.makeText(getContext(), "لطفا تاریخ شروع جست و جو را وارد کنید.", Toast.LENGTH_LONG).show();
            } else {
                isPopupShow = false;
                isFromSet = false;
                isToSet = false;
            }
            isToSet = true;

            toTextView.setText(toTextView.getText() + " " + hourOfDay + ":" + minute);
            endTime += hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000;
        }
    }
}
