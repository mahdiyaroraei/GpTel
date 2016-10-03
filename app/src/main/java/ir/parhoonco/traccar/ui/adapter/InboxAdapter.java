package ir.parhoonco.traccar.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.model.InboxItem;

/**
 * Created by mao on 8/13/2016.
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private ArrayList<InboxItem> inbox;
    private Context context;
    public InboxAdapter(ArrayList<InboxItem> inbox, Context context) {
        this.inbox = inbox;
        this.context = context;
    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new InboxViewHolder(((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_inbox , null));
    }

    @Override
    public void onBindViewHolder(InboxViewHolder holder, int position) {
        holder.titleTextView.setText(inbox.get(position).getTitle());
        holder.descriptionTextView.setText(inbox.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return inbox.size();
    }

    public class InboxViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTextView;
        public TextView descriptionTextView;

        public InboxViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
