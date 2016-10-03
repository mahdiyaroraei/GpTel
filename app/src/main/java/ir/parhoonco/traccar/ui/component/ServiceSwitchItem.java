package ir.parhoonco.traccar.ui.component;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.SharedPreferenceHelper;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.ui.dialog.ConfigDialog;
import ir.parhoonco.traccar.ui.dialog.ErrorDialog;
import ir.parhoonco.traccar.ui.dialog.SuccessDialog;
import ir.parhoonco.traccar.ui.fragment.CarFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/3/2016.
 */
public class ServiceSwitchItem extends LinearLayout {
    public static int disCount = 0;
    public ServiceItem time;
    public ServiceItem distance;
    private TextView nameTextView;
    private String id;
    private ImageView icon;
    private ImageView remove;


    public ServiceSwitchItem(Context context) {
        super(context);
        init();
    }

    public ServiceSwitchItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ServiceSwitchItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setClickable(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfig();
            }
        });
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.item_service_switch_layout, this);
        nameTextView = (TextView) findViewById(R.id.name);
        icon = (ImageView) findViewById(R.id.icon);
        remove = (ImageView) findViewById(R.id.remove);
    }


    public void showConfig() {
        final ConfigDialog dialog = new ConfigDialog();
        dialog.showDialog((Activity) getContext(), "مقادیر مورد نظر خود را انتخاب نمائید.");
        time = new ServiceItem(getContext()).withName(R.string.service_base_time).withIcon(R.drawable.clock).withIdentifier(id + 1);
        distance = new ServiceItem(getContext()).withName(R.string.service_base_distance).withIcon(R.drawable.ic_distance).withIdentifier(id + 2);
        dialog.addView(time);
        dialog.addView(distance);
        dialog.dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceHelper.setSharedPreferenceInt(getContext(), id + "1", Integer.parseInt(time.value.getText().toString()));
                SharedPreferenceHelper.setSharedPreferenceInt(getContext(), id + "2", Integer.parseInt(distance.value.getText().toString()));
                dialog.dimmisDialog();
                Call<Empty> saveCall = ApplicationLoader.api.setPmConfig(CarFragment.defaultImei
                        , id
                        , Integer.parseInt(time.value.getText().toString())
                        , Integer.parseInt(distance.value.getText().toString()));
                saveCall.enqueue(new Callback<Empty>() {
                    @Override
                    public void onResponse(Call<Empty> call, Response<Empty> response) {
                        if (response.code() == 400) {
                            ErrorDialog dialog = new ErrorDialog();
                            dialog.showDialog((Activity) getContext(), R.string.error_happen);
                        } else if (response.code() == 204) {
                            SuccessDialog dialog = new SuccessDialog();
                            dialog.showDialog((Activity) getContext(), "با موفقیت ذخیره شد.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Empty> call, Throwable t) {

                    }
                });
            }
        });
    }

    public ServiceSwitchItem withName(int name) {
        nameTextView.setText(name);
        return this;
    }

    public ServiceSwitchItem withIdentifier(final String id) {
        this.id = id;
        icon.setImageResource(getContext().getResources().getIdentifier(id, "drawable", getContext().getPackageName()));
        remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Empty> call = ApplicationLoader.api.deletePmConfig(CarFragment.defaultImei , id);
                call.enqueue(new Callback<Empty>() {
                    @Override
                    public void onResponse(Call<Empty> call, Response<Empty> response) {
                        if (response.code() == 204){
                            ((ViewGroup) getParent()).removeView(ServiceSwitchItem.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<Empty> call, Throwable t) {

                    }
                });
            }
        });
        return this;
    }

    public String getIdentifire() {
        return id;
    }
}
