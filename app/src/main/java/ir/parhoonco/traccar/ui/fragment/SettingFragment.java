package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.SharedPreferenceHelper;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.LaunchActivity;

/**
 * Created by mao on 8/3/2016.
 */
public class SettingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((LaunchActivity) getActivity()).onTabClicked(R.id.settingsTab);

        View view = inflater.inflate(R.layout.fragment_settings, null);

        view.findViewById(R.id.paymentFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new PaymentFragment());
            }
        });
        view.findViewById(R.id.eventFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new EventFragment());
            }
        });
        view.findViewById(R.id.ticketFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new TicketFragment());
            }
        });
        view.findViewById(R.id.aboutUsFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new AboutUsFragment());
            }
        });
        view.findViewById(R.id.deviceFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new DeviceFragment());
            }
        });
        view.findViewById(R.id.insuranceFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new InsuranceFragment());
            }
        });
        view.findViewById(R.id.commandFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new CommandFragment());
            }
        });
        view.findViewById(R.id.assignmentFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new AssignmentFragment());
            }
        });

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AppCompatDialog dialog = new AppCompatDialog(getContext());
                dialog.setCancelable(true);
                dialog.setContentView(inflater.inflate(R.layout.dialog_logout, null));
                dialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        SharedPreferenceHelper.setSharedPreferenceString(getContext(), "api_key", null);
                        SharedPreferenceHelper.setSharedPreferenceString(getContext(), "default_device_imei", null);
                        FragmentHelper.getInstance(getContext()).addToStack(new PhoneVerifyFragment());
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
}
