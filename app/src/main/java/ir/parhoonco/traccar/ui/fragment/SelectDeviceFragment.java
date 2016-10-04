package ir.parhoonco.traccar.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.SharedPreferenceHelper;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.LaunchActivity;

/**
 * Created by mao on 8/1/2016.
 */
public class SelectDeviceFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((LaunchActivity) getActivity()).hideTabLayout();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_select_device, null);

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> data_imei = new ArrayList<String>();
        for (Device device :
                CarFragment.devices) {
            data_imei.add(device.getImei());
            if (device.getName() == null) {
                data.add("بی نام");
            } else {
                data.add(device.getName());
            }
        }

        final AppCompatSpinner spinner = (AppCompatSpinner) fragmentView.findViewById(R.id.device_list);
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_device_spinner, R.id.device_name, data));

        try {
            spinner.setSelection(data_imei.indexOf(CarFragment.defaultImei));
        } catch (Exception e) {
        }

        fragmentView.findViewById(R.id.submit_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceHelper.setSharedPreferenceString(getContext(), "default_device_imei", CarFragment.devices.get(spinner.getSelectedItemPosition()).getImei());
                ((LaunchActivity) getActivity()).hideTabLayout();
                FragmentHelper.getInstance(getContext()).addToStack(new CarFragment());
            }
        });

        return fragmentView;
    }
}
