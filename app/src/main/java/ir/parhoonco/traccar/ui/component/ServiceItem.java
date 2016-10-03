package ir.parhoonco.traccar.ui.component;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.SharedPreferenceHelper;

/**
 * Created by mao on 8/3/2016.
 */
public class ServiceItem extends RelativeLayout {
    private TextView nameTextView;
    private ImageView icon;
    public EditText value;
    private Button upBtn;
    private Button downBtn;
    private AppCompatCheckBox aSwitch;
    private String id;

    public ServiceItem(Context context) {
        super(context);
        init();
    }

    public ServiceItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ServiceItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.item_service_layout, this);
        nameTextView = (TextView) findViewById(R.id.name);
        icon = (ImageView) findViewById(R.id.icon);
        value = (EditText) findViewById(R.id.valueEditText);
        upBtn = (Button) findViewById(R.id.upBtn);
        upBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int val = Integer.parseInt(value.getText().toString());
                val += 10;
                value.setText(val + "");
            }
        });
        downBtn = (Button) findViewById(R.id.downBtn);
        downBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int val = Integer.parseInt(value.getText().toString());
                val -= 10;
                value.setText(val+ "");
            }
        });
        aSwitch = (AppCompatCheckBox) findViewById(R.id.switchCompat);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    if (ServiceSwitchItem.disCount == 0){
                        ServiceSwitchItem.disCount = 1;
                        value.setText("0");
                        value.setEnabled(b);
                        upBtn.setEnabled(b);
                        downBtn.setEnabled(b);
                    }else {
                        Toast.makeText(getContext() , "حداقل یک سرویس فعال باید وجود داشته باشد.", Toast.LENGTH_LONG).show();
                        aSwitch.setChecked(true);
                    }
                }else {
                    ServiceSwitchItem.disCount = 0;
                    value.setEnabled(b);
                    upBtn.setEnabled(b);
                    downBtn.setEnabled(b);
                }
            }
        });
    }

    public ServiceItem withName(int name) {
        nameTextView.setText(name);
        return this;
    }

    public ServiceItem withIcon(int res) {
        icon.setImageResource(res);
        return this;
    }

    public ServiceItem withIdentifier(String id) {
        this.id = id;
        value.setText(SharedPreferenceHelper.getSharedPreferenceInt(getContext(), id + "", 0) + "");
        return this;
    }

    public ServiceItem disable(boolean v){
        value.setEnabled(!v);
        return this;
    }

}
