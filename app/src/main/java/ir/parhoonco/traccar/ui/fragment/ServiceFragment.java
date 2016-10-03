package ir.parhoonco.traccar.ui.fragment;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.AndroidUtil;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.SharedPreferenceHelper;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Pm;
import ir.parhoonco.traccar.core.model.api.PmConfig;
import ir.parhoonco.traccar.ui.LaunchActivity;
import ir.parhoonco.traccar.ui.component.ServiceItem;
import ir.parhoonco.traccar.ui.component.ServiceSwitchItem;
import ir.parhoonco.traccar.ui.dialog.ConfigDialog;
import ir.parhoonco.traccar.ui.dialog.ErrorDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/3/2016.
 */

public class ServiceFragment extends Fragment implements Callback<List<Pm>> {
    public static String oil = "oil";
    public static String gearoil = "gearoil";
    public static String brakeoil = "brakeoil";
    public static String airfilter = "airfilter";
    public static String oilfilter = "oilfilter";
    public static String timingbelt = "timingbelt";
    public static String brakepad = "brakepad";
    public static String tirechange = "tirechange";
    public static String tireswap = "tireswap";
    public static String antifreeze = "antifreeze";

    private ArrayList<String> pmsKeys = new ArrayList<>();

    private ArrayList<ServiceSwitchItem> pms;
    private ViewPager mPager;
    private LinearLayout layout;
    private LinearLayout bottomScroll;
    private LinearLayout layout_p2;
    private LinearLayout bottomScroll_p2;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((LaunchActivity) getActivity()).onTabClicked(R.id.serviceTab);
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_service, null);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("سرویس های فعال"));
        tabLayout.addTab(tabLayout.newTab().setText("سرویس های انجام شده"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setAdapter(new ServicePagerAdapter());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        initPmKeys();

        return v;
    }

    private void initPmKeys() {
        pmsKeys.add("oil");
        pmsKeys.add("gearoil");
        pmsKeys.add("brakeoil");
        pmsKeys.add("airfilter");
        pmsKeys.add("oilfilter");
        pmsKeys.add("timingbelt");
        pmsKeys.add("brakepad");
        pmsKeys.add("tirechange");
        pmsKeys.add("tireswap");
        pmsKeys.add("antifreeze");
    }

    private void showConfigPms(List<PmConfig> pmConfigs, boolean fromServer) {

        layout.removeAllViews();
        pms = new ArrayList<>();
        for (PmConfig pm :
                pmConfigs) {
            if (fromServer) {
                if (CarFragment.defaultDevice.getPmConfigs().size() > 0) {
                    try {
                        PmConfig pmConfig = PmConfig.find(PmConfig.class, "device = ? and pmkey = ?", String.valueOf(CarFragment.defaultDevice.getId()), pm.getPmkey()).get(0);
                        pmConfig.setDistancethreshold(pm.getDistancethreshold());
                        pmConfig.setStartodometer(pm.getStartodometer());
                        pmConfig.setStarttime(pm.getStarttime());
                        pmConfig.setTimethreshold(pm.getTimethreshold());
                        pmConfig.save();
                    } catch (IndexOutOfBoundsException e) {

                        pm.setDevice(CarFragment.defaultDevice);
                        pm.save();
                    }
                } else {
                    pm.setDevice(CarFragment.defaultDevice);
                    pm.save();
                }
            }
            SharedPreferenceHelper.setSharedPreferenceBoolean(getContext(), pm.getPmkey(), true);
            SharedPreferenceHelper.setSharedPreferenceInt(getContext(), pm.getPmkey() + 1, pm.getTimethreshold());
            SharedPreferenceHelper.setSharedPreferenceInt(getContext(), pm.getPmkey() + 1, pm.getTimethreshold());
            SharedPreferenceHelper.setSharedPreferenceInt(getContext(), pm.getPmkey() + 2, pm.getDistancethreshold());
            pms.add(new ServiceSwitchItem(getContext()).withName(getContext().getResources().getIdentifier(pm.getPmkey(), "string", getContext().getPackageName())).withIdentifier(pm.getPmkey()));
        }
        for (ServiceSwitchItem item :
                pms) {
            layout.addView(item);
        }
        TextView textView = new TextView(getContext());
        textView.setText("افزودن");
        textView.setPadding(0, 0, 50, 0);
        textView.setTextColor(Color.parseColor("#FF2B9934"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 29);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfig();
            }
        });

        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.ic_add);

        bottomScroll.removeAllViews();
        bottomScroll.addView(textView);
//                        bottomScroll.addView(imageView);
    }

    public void showConfig() {
        final ConfigDialog dialog = new ConfigDialog();
        dialog.showDialog((Activity) getContext(), "مقادیر مورد نظر خود را انتخاب نمائید.");
        final AppCompatSpinner spinner = new AppCompatSpinner(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_pm_spinner, R.id.config_name, new String[]{"تعویض روغن", "تعویض واسکازین", "تعویض روغن ترمز"
                , "تعویض فیلتر هوا", "تعویض فیلتر روغن", "تعویض تسمه تایم", "تعویض لنت ترمز", "تعویض تایر", "جابجایی تایر", "ضد یخ"});
        spinner.setAdapter(adapter);
        spinner.setPadding(0, 0, 0, AndroidUtil.dpToPx(17, getResources()));
        dialog.addView(spinner);
        final String id = pmsKeys.get(spinner.getSelectedItemPosition());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                for (int j = 0; j < 2; j++) {
                    dialog.dialogLayout.removeView(dialog.dialogLayout.getChildAt(1));
                }
                final String pmkey = pmsKeys.get(spinner.getSelectedItemPosition());
                final ServiceItem time = new ServiceItem(getContext()).withName(R.string.service_base_time).withIcon(R.drawable.clock).withIdentifier(id + 1);
                final ServiceItem distance = new ServiceItem(getContext()).withName(R.string.service_base_distance).withIcon(R.drawable.ic_distance).withIdentifier(id + 2);
                dialog.addView(time);
                dialog.addView(distance);

                dialog.dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferenceHelper.setSharedPreferenceInt(getContext(), pmkey + "1", Integer.parseInt(time.value.getText().toString()));
                        SharedPreferenceHelper.setSharedPreferenceInt(getContext(), pmkey + "2", Integer.parseInt(distance.value.getText().toString()));
                        dialog.dimmisDialog();
                        Call<Empty> saveCall = ApplicationLoader.api.setPmConfig(CarFragment.defaultImei
                                , pmkey
                                , Integer.parseInt(time.value.getText().toString())
                                , Integer.parseInt(distance.value.getText().toString()));
                        saveCall.enqueue(new Callback<Empty>() {
                            @Override
                            public void onResponse(Call<Empty> call, Response<Empty> response) {
                                if (response.code() == 400) {
                                    ErrorDialog dialog = new ErrorDialog();
                                    dialog.showDialog((Activity) getContext(), R.string.error_happen);
                                } else if (response.code() == 204) {
                                    final Call<List<PmConfig>> listCall = ApplicationLoader.api.getPmConfig(CarFragment.defaultImei);
                                    listCall.enqueue(new Callback<List<PmConfig>>() {
                                        @Override
                                        public void onResponse(Call<List<PmConfig>> call, Response<List<PmConfig>> response) {
                                            if (response.code() == 400) {
                                                ErrorDialog dialog = new ErrorDialog();
                                                dialog.showDialog(getActivity(), R.string.error_happen);

                                            } else if (response.code() == 200) {
                                                showConfigPms(response.body(), true);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<PmConfig>> call, Throwable t) {
                                            showConfigPms(CarFragment.defaultDevice.getPmConfigs(), false);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<Empty> call, Throwable t) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onResponse(Call<List<Pm>> call, Response<List<Pm>> response) {
        showPms(response.body(), true);
    }

    @Override
    public void onFailure(Call<List<Pm>> call, Throwable t) {

    }

    private void showPms(List<Pm> pmList, boolean fromServer) {
        if (!fromServer) {
            layout_p2.removeAllViews();
        }
        bottomScroll_p2.removeAllViews();
        if (pmList != null) {
            for (Pm pm :
                    pmList) {
                // If pm get from server must be stored in db
                if (fromServer) {
                    pm.setDevice(CarFragment.defaultDevice);
                    pm.save();
                }
                View v = getLayoutInflater(null).inflate(R.layout.pm_layout_item, null);
                ((ImageView) v.findViewById(R.id.icon)).setImageResource(getResources().getIdentifier(pm.getPmkey(), "drawable", getContext().getPackageName()));
                ((TextView) v.findViewById(R.id.service_label)).setText(getResources().getIdentifier(pm.getPmkey(), "string", getContext().getPackageName()));
                ((TextView) v.findViewById(R.id.time_label)).setText("1395/6/18 17:35");

                layout_p2.addView(v);
            }
        }

        ArrayList<String> pms = new ArrayList<String>();
        pms.add("oil");
        pms.add("gearoil");
        pms.add("brakeoil");
        pms.add("airfilter");
        pms.add("oilfilter");
        pms.add("timingbelt");
        pms.add("brakepad");
        pms.add("tirechange");
        pms.add("tireswap");
        pms.add("antifreeze");

        ScrollView scrollSelector = new ScrollView(getContext());
        LinearLayout layoutSelector = new LinearLayout(getContext());
        layoutSelector.setOrientation(LinearLayout.VERTICAL);

        layoutSelector.setFocusable(true);
        layoutSelector.setFocusableInTouchMode(true);
        layoutSelector.setClickable(true);

        scrollSelector.addView(layoutSelector);
        for (final String pm :
                pms) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(getResources().getIdentifier(pm, "drawable", getContext().getPackageName()));
            layoutSelector.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Pm> pms1 = CarFragment.defaultDevice.getPms();
                    List<Pm> toRemove = new ArrayList<Pm>();
                    for (Pm pm1 :
                            pms1) {
                        if (!pm1.getPmkey().equals(pm)) {
                            toRemove.add(pm1);
                        }
                    }
                    pms1.removeAll(toRemove);
                    showPms(pms1, false);

                    long sTime;
                    if (pms1.size() > 0) {
                        sTime = pms1.get(pms1.size() - 1).getTime();
                    } else {
                        sTime = 0;
                    }

                    Call<List<Pm>> call = ApplicationLoader.api.getPm(CarFragment.defaultImei, pm, sTime);
                    call.enqueue(ServiceFragment.this);
                }
            });
        }
        bottomScroll_p2.addView(scrollSelector);
    }

    class ServicePagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {

                View v = getLayoutInflater(null).inflate(R.layout.pager_layout, null);
                final LinearLayout bottomScroll = (LinearLayout) v.findViewById(R.id.bottom_scroll);
                ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll);
                final LinearLayout layout = new LinearLayout(getContext());
                layout.setLayoutTransition(new LayoutTransition());
                layout.setOrientation(LinearLayout.VERTICAL);
                scrollView.addView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                final Call<List<PmConfig>> call = ApplicationLoader.api.getPmConfig(CarFragment.defaultImei);

                ServiceFragment.this.layout = layout;
                ServiceFragment.this.bottomScroll = bottomScroll;

                call.enqueue(new Callback<List<PmConfig>>() {
                    @Override
                    public void onResponse(Call<List<PmConfig>> call, Response<List<PmConfig>> response) {
                        if (response.code() == 400) {
                            ErrorDialog dialog = new ErrorDialog();
                            dialog.showDialog(getActivity(), R.string.error_happen);

                        } else if (response.code() == 200) {
                            showConfigPms(response.body(), true);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PmConfig>> call, Throwable t) {
                        showConfigPms(CarFragment.defaultDevice.getPmConfigs(), false);
                    }
                });

                container.addView(v);
                return v;
            } else {

                View v = getLayoutInflater(null).inflate(R.layout.pager_layout, null);
                final LinearLayout bottomScroll = (LinearLayout) v.findViewById(R.id.bottom_scroll);
                ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll);
                final LinearLayout layout = new LinearLayout(getContext());
                layout.setLayoutTransition(new LayoutTransition());
                layout.setOrientation(LinearLayout.VERTICAL);
                scrollView.addView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                ServiceFragment.this.layout_p2 = layout;
                ServiceFragment.this.bottomScroll_p2 = bottomScroll;
                List<Pm> pms = CarFragment.defaultDevice.getPms();
                List<Pm> toRemove = new ArrayList<Pm>();
                for (Pm pm :
                        pms) {
                    if (!pm.getPmkey().equals("oil")) {
                        toRemove.add(pm);
                    }
                }
                pms.removeAll(toRemove);
                showPms(pms, false);
                long sTime = 0;
                if (pms.size() > 0) {
                    sTime = pms.get(pms.size() - 1).getTime() + 1;
                }
                Call<List<Pm>> call = ApplicationLoader.api.getPm(CarFragment.defaultImei, "oil", sTime);
                call.enqueue(ServiceFragment.this);

                container.addView(v);
                return v;
            }
        }
    }
}