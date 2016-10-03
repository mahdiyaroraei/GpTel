package ir.parhoonco.traccar.ui;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.SharedPreferenceHelper;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.Verify;
import ir.parhoonco.traccar.ui.fragment.CarFragment;
import ir.parhoonco.traccar.ui.fragment.MapFragment;
import ir.parhoonco.traccar.ui.fragment.PhoneVerifyFragment;
import ir.parhoonco.traccar.ui.fragment.ServiceFragment;
import ir.parhoonco.traccar.ui.fragment.SettingFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaunchActivity extends AppCompatActivity {
    private LinearLayout tabLayout;
    private LinearLayout offlineLayout;
    private boolean exit = false;
    private ArrayList<View> tabs = new ArrayList<>();
    private View enableTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationLoader.context = this;
        ApplicationLoader.init();

        setContentView(R.layout.activity_launch);

        /*Initialize Offline Layout*/
        offlineLayout = (LinearLayout) findViewById(R.id.offline_layout);
        offlineLayout.findViewById(R.id.remove_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offlineLayout.setVisibility(View.INVISIBLE);
            }
        });

        tabLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tabs.add(tabLayout.findViewById(R.id.carTab));
        tabs.add(tabLayout.findViewById(R.id.locationTab));
        tabs.add(tabLayout.findViewById(R.id.serviceTab));
        tabs.add(tabLayout.findViewById(R.id.settingsTab));

        if (SharedPreferenceHelper.getSharedPreferenceString(this, "api_key", null) == null) {
            FragmentHelper.getInstance(LaunchActivity.this).addToStack(new PhoneVerifyFragment());
        } else {
            Call<Verify> call = ApplicationLoader.api.loginByKey(
                    SharedPreferenceHelper.getSharedPreferenceString(this, "user_id", null)
                    , SharedPreferenceHelper.getSharedPreferenceString(this, "api_key", null)
                    , Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID), "android");

            call.enqueue(new Callback<Verify>() {
                @Override
                public void onResponse(Call<Verify> call, Response<Verify> response) {
                    if (response.code() == 400) {
                        if (response.message().equals("INVALID_APIKEY")) {
                            FragmentHelper.getInstance(LaunchActivity.this).addToStack(new PhoneVerifyFragment());
                        } else {
                            CarFragment.devices = Device.listAll(Device.class);
                            FragmentHelper.getInstance(LaunchActivity.this).addToStack(new CarFragment());
                        }
                    } else if (response.code() == 200) {
                        CarFragment.devices = response.body().getDevices();
                        for (Device device :
                                response.body().getDevices()) {
                            if (Device.find(Device.class, "imei = ?", device.getImei()).size() > 0) {
                                device.setId(Device.find(Device.class, "imei = ?", device.getImei()).get(0).getId());
                            }
                            device.save();
                        }
                        FragmentHelper.getInstance(LaunchActivity.this).addToStack(new CarFragment());
                    }
                }

                @Override
                public void onFailure(Call<Verify> call, Throwable t) {
                    CarFragment.devices = Device.listAll(Device.class);
                    FragmentHelper.getInstance(LaunchActivity.this).addToStack(new CarFragment());
                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "برای خروج دوباره بروی دکمه بازگشت کلیک کنید.", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3000);
            exit = true;
        }
    }

    public void showOfflineLayout() {
        offlineLayout.setVisibility(View.VISIBLE);
    }

    public void hideOfflineLayout() {
        offlineLayout.setVisibility(View.INVISIBLE);
    }

    public void onTabClicked(View v) {
        if (enableTab != null){
            TransitionDrawable transition = (TransitionDrawable) enableTab.getBackground();
            transition.reverseTransition(200);
        }
        TransitionDrawable transition = (TransitionDrawable) v.getBackground();
        transition.startTransition(200);
//        v.setBackgroundResource(R.drawable.tab_selected_bg);
        if (v.getId() == R.id.carTab) {
            FragmentHelper.getInstance(this).addToStack(new CarFragment());
        } else if (v.getId() == R.id.locationTab) {
            FragmentHelper.getInstance(this).addToStack(new MapFragment());
        } else if (v.getId() == R.id.serviceTab) {
            FragmentHelper.getInstance(this).addToStack(new ServiceFragment());
        } else if (v.getId() == R.id.settingsTab) {
            FragmentHelper.getInstance(this).addToStack(new SettingFragment());
        }

        enableTab = v;
    }

    public void onTabClicked(int id) {
        View v = findViewById(id);
        if (enableTab != null){
            TransitionDrawable transition = (TransitionDrawable) enableTab.getBackground();
            transition.reverseTransition(200);
        }

        TransitionDrawable transition = (TransitionDrawable) v.getBackground();
        transition.startTransition(200);

        enableTab = v;
    }

    public void showTabLayout() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void hideTabLayout() {
        tabLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationLoader.resume();
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        ApplicationLoader.pause();
    }
}
