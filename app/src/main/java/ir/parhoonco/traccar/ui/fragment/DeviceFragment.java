package ir.parhoonco.traccar.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.ui.FragmentHelper;

/**
 * Created by mao on 8/1/2016.
 */
public class DeviceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_device, null);
        if (!CarFragment.isMaster) {
            ((TextView) fragmentView.findViewById(R.id.device_config_txt)).setTextColor(Color.parseColor("#898989"));
            ((TextView) fragmentView.findViewById(R.id.user_management)).setTextColor(Color.parseColor("#898989"));
        }
        fragmentView.findViewById(R.id.select_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new SelectDeviceFragment());
            }
        });

        fragmentView.findViewById(R.id.device_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CarFragment.isMaster) {
                    FragmentHelper.getInstance(getContext()).addToStack(new DeviceConfigFragment());
                } else {
                    Toast.makeText(getContext(), "برای دسترسی به این قسمت باید سرگروه باشید.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragmentView.findViewById(R.id.device_users).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CarFragment.isMaster) {
                    FragmentHelper.getInstance(getContext()).addToStack(new DeviceUsersFragment());
                }else {
                    Toast.makeText(getContext(), "برای دسترسی به این قسمت باید سرگروه باشید.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return fragmentView;
    }
}
