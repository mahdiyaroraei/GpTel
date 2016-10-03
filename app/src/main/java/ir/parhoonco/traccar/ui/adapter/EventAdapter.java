package ir.parhoonco.traccar.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.List;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.model.api.Event;

/**
 * Created by mao on 9/14/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_event , null));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.root.setBackgroundColor(context.getResources().getColor(context.getResources().getIdentifier(events.get(position).getType().toLowerCase() , "color" , context.getPackageName())));
        holder.eventTextView.setText(context.getResources().getIdentifier(events.get(position).getType().toLowerCase() , "string" , context.getPackageName()));
        PersianCalendar calendar = new PersianCalendar(events.get(position).getTime() * 1000);
        holder.timeTextView.setText(calendar.getPersianShortDateTime());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder{
        public TextView eventTextView;
        public TextView timeTextView;
        public View root;

        public EventViewHolder(View itemView) {
            super(itemView);

            root = itemView;
            eventTextView = (TextView) itemView.findViewById(R.id.event_txt);
            timeTextView = (TextView) itemView.findViewById(R.id.time_txt);
        }
    }
}
