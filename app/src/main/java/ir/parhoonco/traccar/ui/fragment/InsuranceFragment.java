package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Insurance;
import ir.parhoonco.traccar.ui.dialog.ErrorDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/1/2016.
 */
public class InsuranceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_insurance, null);

        final LinearLayout showLayout = (LinearLayout) fragmentView.findViewById(R.id.show_insurance_layout);
        final LinearLayout addLayout = (LinearLayout) fragmentView.findViewById(R.id.add_insurance_layout);

        final Call<Insurance> insuranceCall = ApplicationLoader.api.getInsurance(CarFragment.defaultImei);
        addLayout.setVisibility(View.INVISIBLE);
        showLayout.setVisibility(View.INVISIBLE);

        final Callback<Insurance> callback = new Callback<Insurance>() {
            @Override
            public void onResponse(Call<Insurance> call, Response<Insurance> response) {
                if (response.code() == 400) {

                } else if (response.code() == 200) {
                    if (response.body().getAddress() != null) {
                        response.body().save();
                        CarFragment.defaultDevice.setInsurance(response.body());
                        CarFragment.defaultDevice.save();
                        showLayout.setVisibility(View.VISIBLE);
                        ((TextView) showLayout.findViewById(R.id.person_name_txt)).setText("نام و نام خانوادگی: " + response.body().getFirstname() + " " + response.body().getLastname());
                        ((TextView) showLayout.findViewById(R.id.person_national_code_txt)).setText("کد ملی: " + response.body().getNationalcode());
                        ((TextView) showLayout.findViewById(R.id.person_address_txt)).setText("آدرس: " + response.body().getAddress());
                    } else {
                        addLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Insurance> call, Throwable t) {
                Insurance insurance = CarFragment.defaultDevice.getInsurance();
                if (insurance != null) {
                    showLayout.setVisibility(View.VISIBLE);
                    ((TextView) showLayout.findViewById(R.id.person_name_txt)).setText("نام و نام خانوادگی: " + insurance.getFirstname() + " " + insurance.getLastname());
                    ((TextView) showLayout.findViewById(R.id.person_national_code_txt)).setText("کد ملی: " + insurance.getNationalcode());
                    ((TextView) showLayout.findViewById(R.id.person_address_txt)).setText("آدرس: " + insurance.getAddress());
                } else {
                    addLayout.setVisibility(View.VISIBLE);
                }
            }
        };
        insuranceCall.enqueue(callback);


        addLayout.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CarFragment.isMaster) {
                    boolean validate = true;
                    String firstname = ((EditText) addLayout.findViewById(R.id.first_name_edt)).getText().toString();
                    String lastname = ((EditText) addLayout.findViewById(R.id.last_name_edt)).getText().toString();
                    String nationalcode = ((EditText) addLayout.findViewById(R.id.national_code_edt)).getText().toString();
                    String address = ((EditText) addLayout.findViewById(R.id.address_edt)).getText().toString();
                    if (firstname.equals("")) {
                        validate = false;
                    } else if (lastname.equals("")) {
                        validate = false;
                    } else if (nationalcode.equals("")) {
                        validate = false;
                    } else if (address.equals("")) {
                        validate = false;
                    }

                    if (validate) {
                        Call<Empty> call = ApplicationLoader.api.addInsurance(CarFragment.defaultImei, firstname, lastname, nationalcode, address);
                        call.enqueue(new Callback<Empty>() {
                            @Override
                            public void onResponse(Call<Empty> call, Response<Empty> response) {
                                if (response.code() == 204) {
                                    insuranceCall.enqueue(callback);
                                }
                            }

                            @Override
                            public void onFailure(Call<Empty> call, Throwable t) {

                            }
                        });
                    } else {
                        ErrorDialog dialog = new ErrorDialog();
                        dialog.showDialog(getActivity(), "لطفا تمام فیلد هارا پر کنید.");
                    }
                } else {
                    Toast.makeText(getContext(), "برای دسترسی به این قسمت باید سرگروه باشید.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return fragmentView;
    }
}
