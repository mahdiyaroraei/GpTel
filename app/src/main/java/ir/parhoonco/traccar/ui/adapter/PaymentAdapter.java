package ir.parhoonco.traccar.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.List;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.model.api.Payment;

/**
 * Created by mao on 8/13/2016.
 */
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<Payment> payments;
    private Context context;

    public PaymentAdapter(List<Payment> payments, Context context) {
        this.payments = payments;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (payments.get(position).getPaytoken() == null) {
            viewType = 0;
        } else {
            viewType = 1;
        }
        return viewType;
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new PaymentViewHolder(((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_payment_success, null));
        } else {
            return new PaymentViewHolder(((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_payment_pending, null));
        }
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, final int position) {
        holder.price.setText(payments.get(position).getPrice() + "");
        if (payments.get(position).getPaytoken() != null) {
            PersianCalendar calendar = new PersianCalendar(payments.get(position).getDuetime() * 1000);
            holder.time.setText(calendar.getPersianShortDateTime());
            holder.url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = payments.get(position).getPaymenturl();
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                }
            });
        } else {
            PersianCalendar calendar = new PersianCalendar(payments.get(position).getPaytime() * 1000);
            holder.time.setText(calendar.getPersianShortDateTime());
        }
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        public TextView price;
        public TextView time;
        public Button url;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price_txt);
            time = (TextView) itemView.findViewById(R.id.time_txt);
            url = (Button) itemView.findViewById(R.id.url_btn);
        }
    }
}
