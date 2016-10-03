package ir.parhoonco.traccar.core.model.api;

/**
 * Created by mao on 9/24/2016.
 */
public class Error {
    String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
