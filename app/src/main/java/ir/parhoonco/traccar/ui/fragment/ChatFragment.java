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
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.List;
import java.util.TimeZone;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Message;
import ir.parhoonco.traccar.core.model.api.MessageCreate;
import ir.parhoonco.traccar.core.model.api.Messages;
import ir.parhoonco.traccar.core.model.api.Ticket;
import ir.parhoonco.traccar.ui.dialog.ErrorDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 8/17/2016.
 */
public class ChatFragment extends Fragment {
    private int ticketId = 65456;
    private EditText messageInput;
    private Ticket currentTicket;
    private List<Message> messages;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_chat, null);

        ticketId = getArguments().getInt("ticket_id");
        currentTicket = Ticket.find(Ticket.class, "ticketid = ?", String.valueOf(ticketId)).get(0);

        messageInput = (EditText) layout.findViewById(R.id.messageInput);
        final RecyclerView chats = (RecyclerView) layout.findViewById(R.id.chats);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        final ChatAdapter adapter = new ChatAdapter();

        chats.setLayoutManager(layoutManager);

        Call<Messages> messagesCall = ApplicationLoader.api.getMessages(ticketId, 0, 100);
        messagesCall.enqueue(new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response) {
                if (response.code() == 400) {
                    ErrorDialog dialog = new ErrorDialog();
                    dialog.showDialog(getActivity(), R.string.error_happen);
                } else if (response.code() == 200) {
                    messages = response.body().getMessages();
                    chats.setAdapter(adapter);
                    for (int i = response.body().getMessages().size() - 1; i >= 0; i--) {
                        final Message message = response.body().getMessages().get(i);
                        message.setTicket(currentTicket);
                        message.save();
                    }
                }
            }

            @Override
            public void onFailure(Call<Messages> call, Throwable t) {
                messages = currentTicket.getMessages();
                chats.setAdapter(adapter);
            }
        });

        layout.findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String msgBody = messageInput.getText().toString();
                Call<MessageCreate> messageCreateCall = ApplicationLoader.api.addMessage(ticketId, msgBody);
                messageCreateCall.enqueue(new Callback<MessageCreate>() {
                    @Override
                    public void onResponse(Call<MessageCreate> call, Response<MessageCreate> response) {
                        if (response.code() == 400) {
                            ErrorDialog dialog = new ErrorDialog();
                            dialog.showDialog(getActivity(), R.string.error_happen);
                        } else if (response.code() == 200) {
                            messageInput.setText("");
                            Message message = new Message();
                            message.setBody(msgBody);
                            message.setTicket(currentTicket);
                            message.setTime(response.body().getTime());
                            message.setMessageid(response.body().getMessageid());
                            message.setSenderid("Me");
                            message.save();

                            messages.add(0, message);
                            adapter.notifyItemInserted(0);
                            layoutManager.scrollToPosition(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageCreate> call, Throwable t) {
                        ErrorDialog dialog = new ErrorDialog();
                        dialog.showDialog(getActivity(), R.string.error_happen);
                    }
                });
            }
        });
        return layout;
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        private final int FROM_TYPE = 0;
        private final int TO_TYPE = 1;

        @Override
        public int getItemViewType(int position) {
            if (messages.get(position).getSenderid().equals("admin")) {
                return FROM_TYPE;
            } else {
                return TO_TYPE;
            }
        }

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int msg_lay_res;
            if (viewType == FROM_TYPE) {
                msg_lay_res = R.layout.item_msg_in;
            } else {
                msg_lay_res = R.layout.item_msg_out;
            }
            return new ChatViewHolder(getLayoutInflater(null).inflate(msg_lay_res, parent, false));
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, final int position) {
            holder.msgTextView.setText(messages.get(position).getBody());
            PersianCalendar calendar = new PersianCalendar(messages.get(position).getTime() * 1000);
            calendar.setTimeZone(TimeZone.getDefault());

            holder.timeTextView.setText(calendar.getPersianShortDateTime());
            Call<Empty> markReadCall = ApplicationLoader.api.markRead(messages.get(position).getMessageid());
            markReadCall.enqueue(new Callback<Empty>() {
                @Override
                public void onResponse(Call<Empty> call, Response<Empty> response) {
                    if (response.code() == 400) {
                    } else if (response.code() == 204) {
                        messages.get(position).setStatus("read");
                        messages.get(position).save();
                    }
                }

                @Override
                public void onFailure(Call<Empty> call, Throwable t) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder {
            public TextView msgTextView;
            public TextView timeTextView;

            public ChatViewHolder(View itemView) {
                super(itemView);

                msgTextView = (TextView) itemView.findViewById(R.id.msg_txt);
                timeTextView = (TextView) itemView.findViewById(R.id.time_txt);
            }
        }
    }
}
