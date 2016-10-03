package ir.parhoonco.traccar.ui.fragment;

import android.graphics.Color;
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
import java.util.TimeZone;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.api.Command;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 9/21/2016.
 */
public class CommandFragment extends Fragment {
    private List<Command> commands;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_command, null);

        RecyclerView commandRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.commands_rv);

        commands = CarFragment.defaultDevice.getCommands();
        final CommandAdapter adapter = new CommandAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        commandRecyclerView.setLayoutManager(layoutManager);
        commandRecyclerView.setAdapter(adapter);

        long sTime;
        if (commands.size() > 0) {
            sTime = commands.get(0).getTime() + 1;
        } else {
            sTime = 0;
        }

        Call<List<Command>> commandCall
                = ApplicationLoader.api.getCommands(CarFragment.defaultImei, sTime);
        commandCall.enqueue(new Callback<List<Command>>() {
            @Override
            public void onResponse(Call<List<Command>> call, Response<List<Command>> response) {
                if (response.code() == 200) {
                    commands.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    for (Command command :
                            response.body()) {
                        command.setDevice(CarFragment.defaultDevice);
                        command.save();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Command>> call, Throwable t) {

            }
        });
        return fragmentView;
    }

    class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.CommandViewHolder> {

        @Override
        public CommandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommandViewHolder(getLayoutInflater(null).inflate(R.layout.item_command, null));
        }

        @Override
        public void onBindViewHolder(CommandViewHolder holder, int position) {
            if (commands.get(position).getStatus().equals("pending")) {
                holder.layout.setBackgroundColor(Color.parseColor("#bc6c15"));
            }
            holder.commandTextView.setText(commands.get(position).getType());
            holder.attrTextView.setText(commands.get(position).getAttributes());
            PersianCalendar calendar = new PersianCalendar(commands.get(position).getTime());
            calendar.setTimeZone(TimeZone.getDefault());
            holder.timeTextView.setText(calendar.getPersianShortDateTime());
        }

        @Override
        public int getItemCount() {
            return commands.size();
        }

        public class CommandViewHolder extends RecyclerView.ViewHolder {
            public TextView commandTextView;
            public TextView attrTextView;
            public TextView timeTextView;
            public View layout;


            public CommandViewHolder(View itemView) {
                super(itemView);
                layout = itemView;
                commandTextView = (TextView) itemView.findViewById(R.id.command_txt);
                attrTextView = (TextView) itemView.findViewById(R.id.attr_txt);
                timeTextView = (TextView) itemView.findViewById(R.id.time_txt);
            }
        }
    }
}
