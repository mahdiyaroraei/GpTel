package ir.parhoonco.traccar.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.TimeZone;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.SharedPreferenceHelper;
import ir.parhoonco.traccar.core.TeltonikaSmsProtocol;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.DeviceConfig;
import ir.parhoonco.traccar.core.model.api.DeviceInfo;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.LaunchActivity;
import ir.parhoonco.traccar.ui.component.DrawView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/1/2016.
 */
public class CarFragment extends Fragment {
    public static List<Device> devices = null;
    public static String defaultImei = null;
    public static Device defaultDevice;
    public static boolean isMaster = false;
    private static int power_state = -1;
    private boolean protection = false;
    private ViewGroup view;
    private ImageView powerImageView;
    private ImageView protectionImageView;
    private ImageView carImageView;
    private ImageView tempratureImageView;
    private RelativeLayout batteryImageView;
    private TextView tempratureTextView;
    private TextView batteryTextView;
    private TextView lastupdateTextView;
    private TextView deviceNameTextView;
    private DrawView drawView;

    public static void refreshCache() {
//        defaultDevice = Device.find(Device.class, defaultImei).get(0);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        ((LaunchActivity)getActivity()).onTabClicked(R.id.carTab);
        defaultImei = SharedPreferenceHelper.getSharedPreferenceString(getContext(), "default_device_imei", null);
        if (defaultImei == null) {
            FragmentHelper.getInstance(getContext()).addToStack(new SelectDeviceFragment());
        } else {
            defaultDevice = Device.find(Device.class, "imei = ?", defaultImei).get(0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_car, null);

        /*Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ((ImageView)view.findViewById(R.id.background_img)).setImageBitmap(AndroidUtil
                .decodeSampledBitmapFromResource(getResources() , R.drawable.background , width , height - AndroidUtil.dpToPx(70 , getResources())));
*/
        initViews(view);

        ((LaunchActivity) getActivity()).showTabLayout();

        if (defaultDevice != null) {
            deviceNameTextView.setText(defaultDevice.getName());
        }
        showDeviceInfo(defaultDevice.getDeviceInfo());
        isMaster = defaultDevice.getDeviceInfo().ismaster();
        Call<DeviceInfo> deviceInfoCall = ApplicationLoader.api.getDeviceInfo(defaultImei);
        deviceInfoCall.enqueue(new Callback<DeviceInfo>() {
            @Override
            public void onResponse(Call<DeviceInfo> call, Response<DeviceInfo> response) {
                if (response.code() == 200) {
                    response.body().save();
                    defaultDevice.setDeviceInfo(response.body());
                    isMaster = response.body().ismaster();
                    defaultDevice.save();

                    showDeviceInfo(response.body());
                }
            }

            @Override
            public void onFailure(Call<DeviceInfo> call, Throwable t) {
            }
        });

        view.findViewById(R.id.powerImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AppCompatDialog dialog = new AppCompatDialog(getContext());
                dialog.setCancelable(true);
                dialog.setContentView(inflater.inflate(R.layout.dialog_send_sms, null));
                dialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (defaultDevice.getDeviceInfo().getEnginstatus() == 1) {
                            try {
                                TeltonikaSmsProtocol.sendCommand(defaultDevice, TeltonikaSmsProtocol.TYPE_POWEROFF, null, getContext());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                TeltonikaSmsProtocol.sendCommand(defaultDevice, TeltonikaSmsProtocol.TYPE_POWERON, null, getContext());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                dialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        // Protection mode only for server
        view.findViewById(R.id.protectionImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AppCompatDialog dialog = new AppCompatDialog(getContext());
                dialog.setCancelable(true);
                dialog.setContentView(inflater.inflate(R.layout.dialog_protection, null));
                dialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        // Must be get device config from server in this fragment
                        final DeviceConfig config = CarFragment.defaultDevice.getDeviceConfig();
                        if (config != null) {
                            if (protection) {
                                config.setProtectionmode(false);
                            } else {
                                config.setProtectionmode(true);
                            }
                            config.save();
                            Call<Empty> protectionCall = ApplicationLoader.api.setDeviceConfig(config.getImei(), config.isProtectionmode(), config.isAutooff(),
                                    Double.parseDouble(config.getMinvoltage()), config.getMaxspeed(), config.isAutogeofence(), config.getMinangle(), config.getMinperiod()
                                    , config.getSendperiod(), config.getMindistance());
                            protectionCall.enqueue(new Callback<Empty>() {
                                @Override
                                public void onResponse(Call<Empty> call, Response<Empty> response) {
                                    if (response.code() == 204) {
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
                    }
                });
                dialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    private void showDeviceInfo(DeviceInfo deviceInfo) {
        PersianCalendar calendar = new PersianCalendar(deviceInfo.getLastupdate() * 1000);
        calendar.setTimeZone(TimeZone.getDefault());
        lastupdateTextView.setText(calendar.getPersianShortDateTime() + " آخرین بروزرسانی ");
        Typeface typeface = Typeface.createFromAsset(lastupdateTextView.getContext().getAssets(), "fonts/Jersey.ttf");
        tempratureTextView.setTypeface(typeface);
        batteryTextView.setTypeface(typeface);

        String temp_prefix = "+";
        int temp_drawable = R.drawable.mild;
        if (deviceInfo.getTemperature() <= 115 && deviceInfo.getTemperature() > 75) {
            temp_drawable = R.drawable.hot;
        } else if (deviceInfo.getTemperature() <= 75 && deviceInfo.getTemperature() > 25) {
            temp_drawable = R.drawable.warm;
        } else if (deviceInfo.getTemperature() <= 25 && deviceInfo.getTemperature() > 8) {
            temp_drawable = R.drawable.mild;
        } else if (deviceInfo.getTemperature() <= 8 && deviceInfo.getTemperature() >= 0) {
            temp_drawable = R.drawable.cold;
        } else if (deviceInfo.getTemperature() < 0 && deviceInfo.getTemperature() >= -55) {
            temp_drawable = R.drawable.freeze;
            temp_prefix = "-";
        }
        tempratureImageView.setImageResource(temp_drawable);
        tempratureTextView.setText(temp_prefix + (int)deviceInfo.getTemperature() + "");

        drawView.setProgress(deviceInfo.getVoltage() / 15000f);
        NumberFormat df = new DecimalFormat("00.00");
        batteryTextView.setText(df.format(deviceInfo.getVoltage() / 1000f )+ "");

        if (deviceInfo.getEnginstatus() == 1) {
            powerImageView.setImageResource(R.drawable.turn_off);
        } else {
            powerImageView.setImageResource(R.drawable.turn_off);
        }

        boolean isDoorOpen = deviceInfo.getDoorstatus() == 1;
        boolean isLightOn = deviceInfo.getEnginstatus() == 1;
        int carImageId;
        if (isDoorOpen && isLightOn) {
            carImageId = R.drawable.car_door_light_on;
        } else if (!isDoorOpen && isLightOn) {
            carImageId = R.drawable.car_light_on;
        } else if (isDoorOpen) {
            carImageId = R.drawable.car_door;
        } else {
            carImageId = R.drawable.car;
        }

        try {
            Picasso.with(getContext()).load(carImageId).into(carImageView);
        } catch (Exception e) {
        }
    }

    private void initViews(ViewGroup view) {
        powerImageView = (ImageView) view.findViewById(R.id.powerImageView);
        protectionImageView = (ImageView) view.findViewById(R.id.protectionImageView);
        carImageView = (ImageView) view.findViewById(R.id.carImageView);
        tempratureImageView = (ImageView) view.findViewById(R.id.tempratureImageView);
        batteryImageView = (RelativeLayout) view.findViewById(R.id.batteryImageView);
        tempratureTextView = (TextView) view.findViewById(R.id.tempratureTextView);
        batteryTextView = (TextView) view.findViewById(R.id.batteryTextView);
        lastupdateTextView = (TextView) view.findViewById(R.id.time_txt);
        deviceNameTextView = (TextView) view.findViewById(R.id.device_name);
        drawView = (DrawView) view.findViewById(R.id.battery_fill);
    }

    @Override
    public void onResume() {
        super.onResume();

//        carMoving(view);
    }

    private void carMoving(View view) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        ImageView asphalt1 = (ImageView) view.findViewById(R.id.asphalt1);
        ImageView asphalt2 = (ImageView) view.findViewById(R.id.asphalt2);

        Animation animation1 = new TranslateAnimation(0, -width, asphalt1.getY(), asphalt1.getY());
        Animation animation2 = new TranslateAnimation(width, 0, asphalt2.getY(), asphalt2.getY());

        animation1.setDuration(2000);
        animation1.setRepeatCount(Animation.INFINITE);
        animation1.setInterpolator(new LinearInterpolator());
        animation2.setDuration(2000);
        animation2.setInterpolator(new LinearInterpolator());
        animation2.setRepeatCount(Animation.INFINITE);

        asphalt1.startAnimation(animation1);
        asphalt2.startAnimation(animation2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
