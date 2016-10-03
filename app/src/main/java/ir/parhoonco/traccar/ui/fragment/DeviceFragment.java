package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.ui.FragmentHelper;

/**
 * Created by mao on 8/1/2016.
 */
public class DeviceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_device , null);

        fragmentView.findViewById(R.id.select_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new SelectDeviceFragment());
            }
        });

        fragmentView.findViewById(R.id.device_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new DeviceConfigFragment());
            }
        });

        fragmentView.findViewById(R.id.device_users).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new DeviceUsersFragment());
            }
        });
        return fragmentView;
    }
}
