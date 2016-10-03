package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.util.MapViewProjection;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.TeltonikaSmsProtocol;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.DeviceConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/1/2016.
 */
public class DeviceConfigFragment extends Fragment {
    private static final String MAP_FILE = "iran.map";
    private AppCompatSpinner dataSendSpinner;
    private EditText maxSpeedEditText;
    private EditText geoLatEditText;
    private EditText geoLonEditText;
    private EditText geoRadEditText;
    private EditText phoneEditText;
    private AppCompatCheckBox protectionCheckBox;
    private AppCompatCheckBox geoFenceCheckBox;
    private AppCompatCheckBox autoOffCheckBox;
    private MapView mapView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_device_config, null);

        bindViews(fragmentView);

        Call<DeviceConfig> deviceConfigCall = ApplicationLoader.api.getDeviceConfig(CarFragment.defaultImei);
        deviceConfigCall.enqueue(new Callback<DeviceConfig>() {
            @Override
            public void onResponse(Call<DeviceConfig> call, Response<DeviceConfig> response) {
                if (response.code() == 200) {
                    response.body().save();
                    CarFragment.defaultDevice.setDeviceConfig(response.body());
                    CarFragment.defaultDevice.save();
                    fillField(CarFragment.defaultDevice.getDeviceConfig());
                }
            }

            @Override
            public void onFailure(Call<DeviceConfig> call, Throwable t) {
                fillField(CarFragment.defaultDevice.getDeviceConfig());
            }
        });

        fragmentView.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DeviceConfig config = initDeviceConfig();
                if (config == null) {
                    return;
                }
                DeviceConfig carDeviceConfig = CarFragment.defaultDevice.getDeviceConfig();

                if (carDeviceConfig == null) {
                    carDeviceConfig = new DeviceConfig();
                    carDeviceConfig.save();
                    CarFragment.defaultDevice.setDeviceConfig(carDeviceConfig);
                    CarFragment.defaultDevice.save();
                }

                if (carDeviceConfig == null || !(config.isProtectionmode() == carDeviceConfig.isProtectionmode())) {
                    DeviceConfig device_config = CarFragment.defaultDevice.getDeviceConfig();
                    device_config.setProtectionmode(protectionCheckBox.isChecked());
                    device_config.save();
                }
                if (carDeviceConfig == null || !(config.isAutogeofence() == carDeviceConfig.isAutogeofence())) {
                    try {
                        TeltonikaSmsProtocol.sendCommand(CarFragment.defaultDevice, TeltonikaSmsProtocol.TYPE_SETAUTOGEOFENCE, geoFenceCheckBox.isChecked() ? "1" : "0", getContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (carDeviceConfig == null || !(config.isAutooff() == carDeviceConfig.isAutooff())) {
                    DeviceConfig device_config = CarFragment.defaultDevice.getDeviceConfig();
                    device_config.setAutooff(autoOffCheckBox.isChecked());
                    device_config.save();
                }
                if (carDeviceConfig == null || !(config.getMaxspeed() == carDeviceConfig.getMaxspeed())) {
                    try {
                        TeltonikaSmsProtocol.sendCommand(CarFragment.defaultDevice, TeltonikaSmsProtocol.TYPE_SETMAXSPEED, maxSpeedEditText.getText().toString(), getContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                int min_per = 50;
                int min_dis = 5000;
                int angle = 50;
                int send_per = 10;

                switch (dataSendSpinner.getSelectedItemPosition()) {
                    case 0:
                        min_per = 10;
                        min_dis = 3000;
                        angle = 30;
                        break;
                    case 1:
                        min_per = 180;
                        min_dis = 3000;
                        angle = 30;
                        break;
                    case 2:
                        min_per = 600;
                        min_dis = 6000;
                        angle = 60;
                        break;
                }

                try {
                    TeltonikaSmsProtocol.sendCommand(CarFragment.defaultDevice, TeltonikaSmsProtocol.TYPE_SETMINPERIOD, String.valueOf(min_per), getContext());
                    TeltonikaSmsProtocol.sendCommand(CarFragment.defaultDevice, TeltonikaSmsProtocol.TYPE_SETMINANGLE, String.valueOf(angle), getContext());
                    TeltonikaSmsProtocol.sendCommand(CarFragment.defaultDevice, TeltonikaSmsProtocol.TYPE_SETMINDISTANCE, String.valueOf(min_dis), getContext());
                    TeltonikaSmsProtocol.sendCommand(CarFragment.defaultDevice, TeltonikaSmsProtocol.TYPE_SETSENDPERIOD, String.valueOf(send_per), getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Call<Empty> call = ApplicationLoader.api.setDeviceConfig(config.getImei(), config.isProtectionmode(), config.isAutooff(),
                        Double.parseDouble(config.getMinvoltage()), config.getMaxspeed(), config.isAutogeofence(), config.getMinangle(), config.getMinperiod()
                        , config.getSendperiod(), config.getMindistance());
                call.enqueue(new Callback<Empty>() {
                    @Override
                    public void onResponse(Call<Empty> call, Response<Empty> response) {
                        if (response.code() == 204) {
                            config.save();
                        } else {
                            config.setStatus("success_local");
                            config.save();
                        }
                    }

                    @Override
                    public void onFailure(Call<Empty> call, Throwable t) {
                        config.setStatus("success_local");
                        config.save();
                    }
                });
            }
        });

        return fragmentView;
    }

    private void bindViews(View fragmentView) {
        dataSendSpinner = (AppCompatSpinner) fragmentView.findViewById(R.id.data_send_mode_spinner);
        ArrayList<String> list = new ArrayList<>();
        list.add("دقیق");
        list.add("متوسط");
        list.add("اقتصادی");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_device_config_spinner, R.id.config_name, list);
        dataSendSpinner.setAdapter(adapter);

        maxSpeedEditText = (EditText) fragmentView.findViewById(R.id.max_speed_edt);
        geoLatEditText = (EditText) fragmentView.findViewById(R.id.geo_fence_lat_edt);
        geoLonEditText = (EditText) fragmentView.findViewById(R.id.geo_fence_lon_edt);
        geoRadEditText = (EditText) fragmentView.findViewById(R.id.geo_fence_rad_edt);
        phoneEditText = (EditText) fragmentView.findViewById(R.id.phone_change_edt);
        protectionCheckBox = (AppCompatCheckBox) fragmentView.findViewById(R.id.protection_alarm_cbx);
        geoFenceCheckBox = (AppCompatCheckBox) fragmentView.findViewById(R.id.geo_fence_alarm_cbx);
        autoOffCheckBox = (AppCompatCheckBox) fragmentView.findViewById(R.id.auto_off_cbx);

        geoLatEditText.setOnTouchListener(new View.OnTouchListener() {
                                              @Override
                                              public boolean onTouch(View view, MotionEvent motionEvent) {
                                                  if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                                      final AppCompatDialog dialog = new AppCompatDialog(getContext());

                                                      mapView = new MapView(getContext());

                                                      mapView.setClickable(true);
                                                      mapView.getMapScaleBar().setVisible(true);
                                                      mapView.setBuiltInZoomControls(true);
                                                      mapView.setZoomLevelMin((byte) 10);
                                                      mapView.setZoomLevelMax((byte) 20);


                                                      // create a tile cache of suitable size
                                                      TileCache tileCache = AndroidUtil.createTileCache(getContext(), "mapcache",
                                                              mapView.getModel().displayModel.getTileSize(), 1f,
                                                              mapView.getModel().frameBufferModel.getOverdrawFactor());
                                                      if (!new File(Environment.getExternalStorageDirectory(), MAP_FILE).exists()) {
                                                          Toast.makeText(getContext(), "باید ابتدا نقشه را دانلود کنید", Toast.LENGTH_LONG).show();
                                                      } else {
                                                          MapDataStore mapDataStore = new MapFile(new File(Environment.getExternalStorageDirectory(), MAP_FILE));
                                                          TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                                                                  mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
                                                          tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

                                                          // only once a layer is associated with a mapView the rendering starts
                                                          mapView.getLayerManager().getLayers().add(tileRendererLayer);
                                                          mapView.setZoomLevel((byte) 15);
                                                          mapView.setOnTouchListener(new View.OnTouchListener() {
                                                              private float startX;
                                                              private float startY;

                                                              @Override
                                                              public boolean onTouch(View view, MotionEvent motionEvent) {
                                                                  int actionType = motionEvent.getAction();
                                                                  switch (actionType) {
                                                                      case MotionEvent.ACTION_DOWN:
                                                                          startX = motionEvent.getX();
                                                                          startY = motionEvent.getY();
                                                                          return false;

                                                                      case MotionEvent.ACTION_UP:
                                                                          float endX = motionEvent.getX();
                                                                          float endY = motionEvent.getY();

                                                                          if (isAClick(startX, endX, startY, endY)) {
                                                                              LatLong latLong = new MapViewProjection(mapView).fromPixels(motionEvent.getX(), motionEvent.getY()); // the error is here
                                                                              DecimalFormat df = new DecimalFormat("00.00000");
                                                                              geoLatEditText.setText(String.valueOf(df.format(latLong.getLatitude())));
                                                                              geoLonEditText.setText(String.valueOf(df.format(latLong.getLongitude())));
                                                                              dialog.dismiss();
                                                                          }
                                                                  }
                                                                  return false;
                                                              }

                                                              private boolean isAClick(float startX, float endX, float startY, float endY) {
                                                                  float differenceX = Math.abs(startX - endX);
                                                                  float differenceY = Math.abs(startY - endY);
                                                                  if (differenceX > 10 || differenceY > 10) {
                                                                      return false;
                                                                  }
                                                                  return true;
                                                              }
                                                          });
                                                          mapView.setCenter(new LatLong(36.2605, 59.6168));
                                                          dialog.setContentView(mapView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                          dialog.show();
                                                      }
                                                  }

                                                  return true;
                                              }
                                          }

        );
    }

    private void fillField(DeviceConfig config) {
        if (config != null) {
            int select = 0;
            if (config.getMinangle().equals("min")) {
                select = 2;
            } else if (config.getMinangle().equals("mid")) {
                select = 1;
            } else if (config.getMinangle().equals("max")) {
                select = 0;
            }
            dataSendSpinner.setSelection(select);
            maxSpeedEditText.setText(config.getMaxspeed() + "");
            protectionCheckBox.setChecked(config.isProtectionmode());
            autoOffCheckBox.setChecked(config.isAutooff());
            geoFenceCheckBox.setChecked(config.isAutogeofence());
        }
    }

    private DeviceConfig initDeviceConfig() {
        if (maxSpeedEditText.getText().toString().equals("")) {
            Toast.makeText(getContext(), "لطفا مقدار حداکثر سرعت مجاز را وارد نمائید.", Toast.LENGTH_SHORT).show();
            return null;
        }

        DeviceConfig config = new DeviceConfig();
        config.setImei(CarFragment.defaultImei);
        config.setMinvoltage("9800");

        String mode = "min";
        switch (dataSendSpinner.getSelectedItemPosition()) {
            case 0:
                mode = "max";
                break;
            case 1:
                mode = "mid";
                break;
            case 2:
                mode = "min";
                break;
        }
        config.setMinangle(mode);
        config.setMindistance(mode);
        config.setMinperiod(mode);
        config.setSendperiod(mode);

        config.setMaxspeed(Integer.parseInt(maxSpeedEditText.getText().toString()));
        config.setProtectionmode(protectionCheckBox.isChecked());
        config.setAutooff(protectionCheckBox.isChecked());
        config.setAutogeofence(geoFenceCheckBox.isChecked());

        CarFragment.defaultDevice.setDeviceConfig(config);
        CarFragment.defaultDevice.save();

        return config;
    }
}
