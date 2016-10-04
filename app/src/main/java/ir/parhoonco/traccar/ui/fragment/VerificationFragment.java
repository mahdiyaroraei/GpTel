package ir.parhoonco.traccar.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.annotation.Annotation;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.SharedPreferenceHelper;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.Error;
import ir.parhoonco.traccar.core.model.api.Verify;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.component.Timer;
import ir.parhoonco.traccar.ui.dialog.ErrorDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by mao on 8/1/2016.
 */
public class VerificationFragment extends Fragment implements Callback<Verify> {
    public static String phone;
    public static TextView timerTextView;
    private EditText editText0;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private Button submitBtn;
    private Timer timer;

    private EditText nextEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_verification, null);

        fragmentView.findViewById(R.id.back_to_phone_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.getInstance(getContext()).addToStack(new PhoneVerifyFragment());
                timer.cancel();
            }
        });

        editText0 = (EditText) fragmentView.findViewById(R.id.verifyCode0);
        editText1 = (EditText) fragmentView.findViewById(R.id.verifyCode1);
        editText2 = (EditText) fragmentView.findViewById(R.id.verifyCode2);
        editText3 = (EditText) fragmentView.findViewById(R.id.verifyCode3);
        editText4 = (EditText) fragmentView.findViewById(R.id.verifyCode4);

        timerTextView = (TextView) fragmentView.findViewById(R.id.timer_txt);
        timer = new Timer(2 * 60 * 1000, 1000, getContext());
        timer.start();

        nextEditText = editText1;

        View.OnKeyListener onKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (nextEditText == editText1) {
                        nextEditText = editText0;
                        editText4.requestFocus();
                    } else if (nextEditText == editText2) {
                        nextEditText = editText1;
                        editText0.requestFocus();
                    } else if (nextEditText == editText3) {
                        nextEditText = editText2;
                        editText1.requestFocus();
                    } else if (nextEditText == editText4) {
                        nextEditText = editText3;
                        editText2.requestFocus();
                    } else if (nextEditText == editText0) {
                        nextEditText = editText4;
                        editText3.requestFocus();
                    }
                }
                return false;
            }
        };

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(editable.toString().equals(""))) {
                    if (!checkCompleteEditTexts()) {
                        nextEditText.requestFocus();
                        if (nextEditText == editText1) {
                            nextEditText = editText2;
                        } else if (nextEditText == editText2) {
                            nextEditText = editText3;
                        } else if (nextEditText == editText3) {
                            nextEditText = editText4;
                        } else if (nextEditText == editText4) {
                            nextEditText = editText0;
                        } else if (nextEditText == editText0) {
                            nextEditText = editText1;
                        }
                    }
                }
            }

            private boolean checkCompleteEditTexts() {
                if (!(editText0.getText().toString().equals("")) && !(editText1.getText().toString().equals(""))
                        && !(editText2.getText().toString().equals("")) && !(editText3.getText().toString().equals(""))
                        && !(editText4.getText().toString().equals(""))) {
                    submitBtn.performClick();
                    return true;
                }
                return false;
            }
        };

        editText0.addTextChangedListener(textWatcher);
        editText1.addTextChangedListener(textWatcher);
        editText2.addTextChangedListener(textWatcher);
        editText3.addTextChangedListener(textWatcher);
        editText4.addTextChangedListener(textWatcher);

        editText0.setOnKeyListener(onKeyListener);
        editText1.setOnKeyListener(onKeyListener);
        editText2.setOnKeyListener(onKeyListener);
        editText3.setOnKeyListener(onKeyListener);
        editText4.setOnKeyListener(onKeyListener);

        final LinearLayout edt_layout = (LinearLayout) fragmentView.findViewById(R.id.edt_layout);

        editText0.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editText0.getText().clear();
                    edt_layout.setBackgroundColor(Color.parseColor("#383838"));
                }
            }
        });
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editText1.getText().clear();
                }
            }
        });
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editText2.getText().clear();
                }
            }
        });
        editText3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editText3.getText().clear();
                }
            }
        });
        editText4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editText4.getText().clear();
                }
            }
        });

        editText0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    nextEditText = editText1;
                }
                return false;
            }
        });

        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    nextEditText = editText2;
                }
                return false;
            }
        });

        editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    nextEditText = editText3;
                }
                return false;
            }
        });

        editText3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    nextEditText = editText4;
                }
                return false;
            }
        });

        editText4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    nextEditText = editText0;
                }
                return false;
            }
        });

        submitBtn = (Button) fragmentView.findViewById(R.id.send_code_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if no view has focus:
                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                String code = editText0.getText().toString() + editText1.getText().toString() + editText2.getText().toString()
                        + editText3.getText().toString() + editText4.getText().toString();
                Call<Verify> call = ApplicationLoader.api.loginByCode(phone, code,
                        Settings.Secure.getString(getContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID), "android");
                call.enqueue(VerificationFragment.this);

                submitBtn.setText("لطفا صبر کنید...");
                submitBtn.setEnabled(false);
            }
        });

        return fragmentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onResponse(Call<Verify> call, Response<Verify> response) {
        if (response.code() == 400) {
            editText0.setText("");
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");
            editText4.setText("");

            editText0.requestFocus();

            ErrorDialog dialog = new ErrorDialog();

            Converter<ResponseBody, Error> errorConverter =
                    ApplicationLoader.retrofit.responseBodyConverter(Error.class, new Annotation[0]);
            try {
                Error error = errorConverter.convert(response.errorBody());
                dialog.showDialogMessage(getActivity(), error.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            submitBtn.setText("ارسال");
            submitBtn.setEnabled(true);
        } else if (response.code() == 200) {
            CarFragment.devices = response.body().getDevices();
            timer.cancel();
            for (Device device :
                    response.body().getDevices()) {
                device.save();
            }
            SharedPreferenceHelper.setSharedPreferenceString(getContext(), "user_id", phone);
//            SharedPreferenceHelper.setSharedPreferenceString(getContext(), "api_key", CryptHelper.getInstance().encrypt(response.body().getApikey()));
            SharedPreferenceHelper.setSharedPreferenceString(getContext(), "api_key", response.body().getApikey());

            FragmentHelper.getInstance(getActivity()).addToStack(new CarFragment());
        }
    }

    @Override
    public void onFailure(Call<Verify> call, Throwable t) {
        ErrorDialog dialog = new ErrorDialog();
        dialog.showDialog(getActivity(), R.string.check_internet);

        submitBtn.setText("ارسال");
        submitBtn.setEnabled(true);
    }
}
