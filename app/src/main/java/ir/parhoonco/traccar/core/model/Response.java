package ir.parhoonco.traccar.core.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by mao on 8/17/2016.
 */
public class Response {
    @DatabaseField(generatedId = true, id = true)
    private long id;
    @DatabaseField
    private String command;
    @DatabaseField
    private String response;
    @DatabaseField
    private long time;

    public Response() {
    }

    public Response(String command, String response) {
        this.command = command;
        this.response = response;
        time = System.currentTimeMillis();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
