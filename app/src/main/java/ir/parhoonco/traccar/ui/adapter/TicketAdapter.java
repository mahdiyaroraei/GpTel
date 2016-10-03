package ir.parhoonco.traccar.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.model.api.Ticket;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.fragment.ChatFragment;

/**
 * Created by mao on 8/13/2016.
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<Ticket> tickets;
    private Context context;
    public TicketAdapter(List<Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TicketViewHolder(((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_ticket , null));
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, final int position) {
        holder.titleTextView.setText(tickets.get(position).getSubject());
        holder.descriptionTextView.setText(tickets.get(position).getBody());
        if (tickets.get(position).getStatus().equals("read")){
            holder.status.setVisibility(View.INVISIBLE);
        }
        PersianCalendar cal = new PersianCalendar(tickets.get(position).getLastupdate());
        String date = cal.getPersianDay() + " " + cal.getPersianMonthName();
        holder.timeTextView.setText(date);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ticket_id" , tickets.get(position).getTicketid());
                chatFragment.setArguments(bundle);
                FragmentHelper.getInstance(context).addToStack(chatFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView timeTextView;
        public ImageView status;
        public LinearLayout mainLayout;

        public TicketViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            timeTextView = (TextView) itemView.findViewById(R.id.time_txt);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            status = (ImageView) itemView.findViewById(R.id.status_img);
        }
    }
}
