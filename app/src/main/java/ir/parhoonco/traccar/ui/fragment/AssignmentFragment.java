package ir.parhoonco.traccar.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.User;
import ir.parhoonco.traccar.ui.LaunchActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 9/21/2016.
 */
public class AssignmentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_assignment, null);


        final LinearLayout layout = (LinearLayout) fragmentView.findViewById(R.id.scroll_layout);

        Call<Device> deviceCall = ApplicationLoader.api.getDevice(CarFragment.defaultImei);
        deviceCall.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 400) {

                } else if (response.code() == 200) {
                    for (final User user
                            : response.body().getUsers()) {
                        final View userItem = inflater.inflate(R.layout.item_user_assgnment, null);
                        userItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AppCompatDialog dialog = new AppCompatDialog(getContext());
                                dialog.setCancelable(true);
                                dialog.setContentView(inflater.inflate(R.layout.dialog_asssignment, null));
                                dialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        final boolean isCompeleteAssignment = ((AppCompatCheckBox) dialog.findViewById(R.id.delete_group_cbx)).isChecked();
                                        Call<Empty> masterCall = ApplicationLoader.api.setMaster(CarFragment.defaultImei, user.getPhonenumber(), isCompeleteAssignment);
                                        masterCall.enqueue(new Callback<Empty>() {
                                            @Override
                                            public void onResponse(Call<Empty> call, Response<Empty> response) {
                                                if (response.code() == 200) {
                                                    getContext().startActivity(new Intent(getContext(), LaunchActivity.class));
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Empty> call, Throwable t) {

                                            }
                                        });
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
                        ((TextView) userItem.findViewById(R.id.phone_txt)).setText(user.getName() + " : " + user.getPhonenumber());
                        layout.addView(userItem);
                    }
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {

            }
        });

        return fragmentView;
    }
}