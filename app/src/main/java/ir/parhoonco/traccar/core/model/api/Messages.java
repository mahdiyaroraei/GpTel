package ir.parhoonco.traccar.core.model.api;

import java.util.ArrayList;

/**
 * Created by mao on 9/6/2016.
 */
public class Messages {
    private int messagescount;
    private ArrayList<Message> messages;

    public Messages(int messagescount, ArrayList<Message> messages) {
        this.messagescount = messagescount;
        this.messages = messages;
    }

    public int getMessagescount() {
        return messagescount;
    }

    public void setMessagescount(int messagescount) {
        this.messagescount = messagescount;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
