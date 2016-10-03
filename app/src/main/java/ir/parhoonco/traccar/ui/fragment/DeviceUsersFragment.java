package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/1/2016.
 */
public class DeviceUsersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_device_users, null);

        final LinearLayout layout = (LinearLayout) fragmentView.findViewById(R.id.scroll_layout);

        Call<Device> deviceCall = ApplicationLoader.api.getDevice(CarFragment.defaultImei);
        deviceCall.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 400) {

                } else if (response.code() == 200) {
                    for (final User user
                            : response.body().getUsers()) {
                        final View userItem = inflater.inflate(R.layout.item_user, null);
                        userItem.findViewById(R.id.remove_img).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Call<Empty> removeCall = ApplicationLoader.api.removeUserFromGroup(user.getPhonenumber() , CarFragment.defaultImei);
                                removeCall.enqueue(new Callback<Empty>() {
                                    @Override
                                    public void onResponse(Call<Empty> call, Response<Empty> response) {
                                        if (response.code() == 204){
                                            ((ViewGroup) userItem.getParent()).removeView(userItem);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Empty> call, Throwable t) {

                                    }
                                });
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

        final Button addBtn = (Button) fragmentView.findViewById(R.id.add_user_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBtn.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.ic_save , 0);
                final EditText phoneEditText = new EditText(getContext());
                ImageView removeAddImageView = new ImageView(getContext());
                removeAddImageView.setImageResource(R.drawable.remove);
                final LinearLayout addLayout = new LinearLayout(getContext());
                addLayout.setOrientation(LinearLayout.HORIZONTAL);
                addLayout.addView(phoneEditText, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                addLayout.setGravity(Gravity.CENTER_VERTICAL);
                addLayout.addView(removeAddImageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((LinearLayout) fragmentView.findViewById(R.id.add_layout)).addView(addLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                removeAddImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((LinearLayout) fragmentView.findViewById(R.id.add_layout)).removeView(addLayout);
                        addBtn.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.ic_add_circle , 0);
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String phone = phoneEditText.getText().toString();
                        Call<Empty> call = ApplicationLoader.api.addUserToGroup(phone , CarFragment.defaultImei);
                        call.enqueue(new Callback<Empty>() {
                            @Override
                            public void onResponse(Call<Empty> call, Response<Empty> response) {
                                if (response.code() == 204){
                                    final View userItem = inflater.inflate(R.layout.item_user, null);
                                    userItem.findViewById(R.id.remove_img).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Call<Empty> removeCall = ApplicationLoader.api.removeUserFromGroup(phone , CarFragment.defaultImei);
                                            removeCall.enqueue(new Callback<Empty>() {
                                                @Override
                                                public void onResponse(Call<Empty> call, Response<Empty> response) {
                                                    if (response.code() == 204){
                                                        ((ViewGroup) userItem.getParent()).removeView(userItem);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Empty> call, Throwable t) {

                                                }
                                            });
                                        }
                                    });
                                    ((TextView) userItem.findViewById(R.id.phone_txt)).setText(phone);
                                    layout.addView(userItem);
                                }
                            }

                            @Override
                            public void onFailure(Call<Empty> call, Throwable t) {

                            }
                        });
                        ((LinearLayout) fragmentView.findViewById(R.id.add_layout)).removeView(addLayout);
                        addBtn.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.ic_add_circle , 0);
                    }
                });
            }
        });
        return fragmentView;
    }
}
