package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.List;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.api.Credit;
import ir.parhoonco.traccar.core.model.api.Payment;
import ir.parhoonco.traccar.ui.adapter.PaymentAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/1/2016.
 */
public class PaymentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_payment, null);

        Call<Credit> creditCall = ApplicationLoader.api.getCredit(CarFragment.defaultImei);
        creditCall.enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {
                if (response.code() == 200) {
                    response.body().save();
                    CarFragment.defaultDevice.setCredit(response.body());
                    CarFragment.defaultDevice.save();
                    PersianCalendar calendar = new PersianCalendar(response.body().getTime() * 1000);
                    ((TextView) fragmentView.findViewById(R.id.time_txt)).setText(calendar.getPersianShortDateTime());
                    ((TextView) fragmentView.findViewById(R.id.price_txt)).setText(response.body().getAmount() + "ریال");
                }
            }

            @Override
            public void onFailure(Call<Credit> call, Throwable t) {
                Credit credit = CarFragment.defaultDevice.getCredit();
                if (credit != null) {
                    PersianCalendar calendar = new PersianCalendar(credit.getTime() * 1000);
                    ((TextView) fragmentView.findViewById(R.id.time_txt)).setText(calendar.getPersianShortDateTime());
                    ((TextView) fragmentView.findViewById(R.id.price_txt)).setText(credit.getAmount() + "ریال");
                }
            }
        });

        final RecyclerView paymentTable = (RecyclerView) fragmentView.findViewById(R.id.payedTable);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        paymentTable.setLayoutManager(layoutManager);
        Call<List<Payment>> paymentsCall = ApplicationLoader.api.getPayments(CarFragment.defaultImei);
        paymentsCall.enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if (response.code() == 200) {
                    for (Payment payment :
                            response.body()) {
                        payment.setDevice(CarFragment.defaultDevice);
                        payment.save();
                    }
                    PaymentAdapter adapter = new PaymentAdapter(response.body(), getContext());
                    paymentTable.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                List<Payment> payments = CarFragment.defaultDevice.getPayments();
                PaymentAdapter adapter = new PaymentAdapter(payments, getContext());
                paymentTable.setAdapter(adapter);
            }
        });

        return fragmentView;
    }
}
