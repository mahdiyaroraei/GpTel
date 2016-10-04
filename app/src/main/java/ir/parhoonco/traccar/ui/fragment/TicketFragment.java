package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.api.Ticket;
import ir.parhoonco.traccar.core.model.api.TicketCreate;
import ir.parhoonco.traccar.core.model.api.Tickets;
import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.adapter.TicketAdapter;
import ir.parhoonco.traccar.ui.dialog.ConfigDialog;
import ir.parhoonco.traccar.ui.dialog.ErrorDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/1/2016.
 */
public class TicketFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, null);

        final RecyclerView ticketList = (RecyclerView) view.findViewById(R.id.ticketList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ticketList.setLayoutManager(layoutManager);

        view.findViewById(R.id.btn_create_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ConfigDialog dialog = new ConfigDialog();
                dialog.showDialog(getActivity(), "برای ارسال پیام خود فرم زیر را کامل نمائید.");
                dialog.addView(inflater.inflate(R.layout.item_ticket_send, null));
                dialog.dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dimmisDialog();
                        Call<TicketCreate> ticketCreateCall = ApplicationLoader.api.createTicket(
                                ((EditText) dialog.dialogLayout.findViewById(R.id.edit_subject)).getText().toString()
                                , ((EditText) dialog.dialogLayout.findViewById(R.id.edit_body)).getText().toString()
                                , "admin");
                        Ticket ticket = new Ticket();
                        ticket.setSubject(((EditText) dialog.dialogLayout.findViewById(R.id.edit_subject)).getText().toString());
                        ticket.setBody(((EditText) dialog.dialogLayout.findViewById(R.id.edit_body)).getText().toString());
                        ticket.setReceiverid("admin");
                        ticket.save();
                        ticketCreateCall.enqueue(new Callback<TicketCreate>() {
                            @Override
                            public void onResponse(Call<TicketCreate> call, Response<TicketCreate> response) {
                                if (response.code() == 200) {
                                    Fragment chatFragment = new ChatFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("ticket_id", response.body().getTicketid());
                                    chatFragment.setArguments(bundle);
                                    FragmentHelper.getInstance(getContext()).addToStack(chatFragment);
                                }
                            }

                            @Override
                            public void onFailure(Call<TicketCreate> call, Throwable t) {
                                ErrorDialog dialog = new ErrorDialog();
                                dialog.showDialog(getActivity(), R.string.check_internet);
                            }
                        });
                    }
                });
            }
        });

        Call<Tickets> ticketsCall = ApplicationLoader.api.getTickets(0, 20);
        ticketsCall.enqueue(new Callback<Tickets>() {
            @Override
            public void onResponse(Call<Tickets> call, Response<Tickets> response) {

                if (response.code() == 200) {
                    TicketAdapter adapter = new TicketAdapter(response.body().getTickets(), getContext());
                    ticketList.setAdapter(adapter);
                    saveTicketsToDb(response.body().getTickets());
                }
            }

            private void saveTicketsToDb(List<Ticket> tickets) {
                for (Ticket ticket :
                        tickets) {
                    try {
                        ticket.setId(Ticket.find(Ticket.class, "ticketid = ?", String.valueOf(ticket.getTicketid())).get(0).getId());
                    } catch (IndexOutOfBoundsException e) {
                    }
                    ticket.save();
                }
            }

            @Override
            public void onFailure(Call<Tickets> call, Throwable t) {
                List<Ticket> tickets = Ticket.listAll(Ticket.class);
                TicketAdapter adapter = new TicketAdapter(tickets, getContext());
                ticketList.setAdapter(adapter);
            }
        });
        return view;
    }
}
