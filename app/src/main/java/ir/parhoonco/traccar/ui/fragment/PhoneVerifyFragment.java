package ir.parhoonco.traccar.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.lang.annotation.Annotation;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Error;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.LaunchActivity;
import ir.parhoonco.traccar.ui.dialog.ErrorDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Parhoon on 7/26/2016.
 */
public class PhoneVerifyFragment extends Fragment implements Callback<Empty> {
    private EditText editText;
    private Button submitBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((LaunchActivity) getActivity()).hideTabLayout();
        View fragmentView = inflater.inflate(R.layout.fragment_verify_phone, null);

        editText = (EditText) fragmentView.findViewById(R.id.input_phone);
        editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        final LinearLayout edt_layout = (LinearLayout) fragmentView.findViewById(R.id.edt_layout);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_layout.setBackgroundColor(Color.parseColor("#383838"));
            }
        });

        submitBtn = (Button) fragmentView.findViewById(R.id.req_code_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = editText.getText().toString().replace("(", "").replace(")", "").replace("-", "").replace(" ", "");

                VerificationFragment.phone = phone;

                Call<Empty> call = ApplicationLoader.api.verifyPhone(phone);
                call.enqueue(PhoneVerifyFragment.this);

                submitBtn.setText("لطفا صبر کنید...");
                submitBtn.setEnabled(false);

            }
        });
        return fragmentView;
    }

    @Override
    public void onResponse(Call<Empty> call, Response<Empty> response) {
        if (response.code() == 204) {
            FragmentHelper.getInstance(getActivity()).addToStack(new VerificationFragment());
        } else if (response.code() == 400) {
            submitBtn.setText("ارسال");
            submitBtn.setEnabled(true);
        }
    }

    @Override
    public void onFailure(Call<Empty> call, Throwable t) {
        ErrorDialog dialog = new ErrorDialog();
        dialog.showDialog(getActivity(), R.string.check_internet);

        submitBtn.setText("ارسال");
        submitBtn.setEnabled(true);
    }
}
