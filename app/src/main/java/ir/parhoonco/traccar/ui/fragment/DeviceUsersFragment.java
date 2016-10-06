package ir.parhoonco.traccar.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.TeltonikaSmsProtocol;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.User;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.LaunchActivity;
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

        Call<List<User>> deviceCall = ApplicationLoader.api.getUsers(CarFragment.defaultImei);
        deviceCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 400) {

                } else if (response.code() == 200) {
                    for (final User user
                            : response.body()) {
                        final View userItem = inflater.inflate(R.layout.item_user, null);
                        userItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AppCompatDialog dialog = new AppCompatDialog(getContext());
                                dialog.setCancelable(true);
                                dialog.setContentView(inflater.inflate(R.layout.dialog_change_master, null));
                                dialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        Call<Empty> masterCall = ApplicationLoader.api.setMaster(CarFragment.defaultImei, user.getUserid(), false);
                                        masterCall.enqueue(new Callback<Empty>() {
                                            @Override
                                            public void onResponse(Call<Empty> call, Response<Empty> response) {
                                                if (response.code() == 204) {
                                                    FragmentHelper.getInstance(getContext()).addToStack(new CarFragment());
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
                        if (user.ismaster()) {
                            ((ImageView) userItem.findViewById(R.id.icon)).setImageResource(R.drawable.ic_man_master);
                        }
                        userItem.findViewById(R.id.remove_img).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Call<Empty> removeCall = ApplicationLoader.api.removeUserFromGroup(user.getUserid(), CarFragment.defaultImei);
                                removeCall.enqueue(new Callback<Empty>() {
                                    @Override
                                    public void onResponse(Call<Empty> call, Response<Empty> response) {
                                        if (response.code() == 204) {
                                            ((ViewGroup) userItem.getParent()).removeView(userItem);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Empty> call, Throwable t) {

                                    }
                                });
                            }
                        });
                        ((TextView) userItem.findViewById(R.id.phone_txt)).setText(user.getName() + " : " + user.getUserid());
                        layout.addView(userItem);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

        final Button addBtn = (Button) fragmentView.findViewById(R.id.add_user_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_save, 0);
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
                        addBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_circle, 0);
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String phone = phoneEditText.getText().toString();
                        Call<Empty> call = ApplicationLoader.api.addUserToGroup(phone, CarFragment.defaultImei);
                        call.enqueue(new Callback<Empty>() {
                            @Override
                            public void onResponse(Call<Empty> call, Response<Empty> response) {
                                if (response.code() == 204) {
                                    final View userItem = inflater.inflate(R.layout.item_user, null);
                                    userItem.findViewById(R.id.remove_img).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Call<Empty> removeCall = ApplicationLoader.api.removeUserFromGroup(phone, CarFragment.defaultImei);
                                            removeCall.enqueue(new Callback<Empty>() {
                                                @Override
                                                public void onResponse(Call<Empty> call, Response<Empty> response) {
                                                    if (response.code() == 204) {
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
                        addBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_circle, 0);
                    }
                });
            }
        });
        return fragmentView;
    }
}
