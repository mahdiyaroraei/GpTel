package ir.parhoonco.traccar.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.parhoonco.traccar.core.model.api.Command;

/**
 * Created by mao on 8/8/2016.
 */
public class CommandHelper {
    public static String GET_GPS = "getgps";
    public static String PWER_OFF = "poweroff";
    public static String PWER_ON = "poweron";
    private static CommandHelper instance;

    public static final String COMMAND_PHONE_VERIFY = "phoneNumberVerification";
    public static final String COMMAND_FORGROUND_LOGIN = "phoneNumberVerification";
    public static final String COMMAND_BACKGROUND_LOGIN = "phoneNumberVerification";
    public static final String COMMAND_CHANGE_USERNAME = "phoneNumberVerification";//TODO
    public static final String COMMAND_GET_DEVICE_INFO = "phoneNumberVerification";
    public static final String COMMAND_GET_DEVICE_GROUP = "phoneNumberVerification";//TODO
    public static final String COMMAND_GET_DEVICE_POSITION = "phoneNumberVerification";
    public static final String COMMAND_GET_DEVICE_POSITION_ATTRIBUTE = "phoneNumberVerification";//TODO
    public static final String COMMAND_GET_DEVICE_POSITION_HISTORY = "phoneNumberVerification";
    public static final String COMMAND_GET_DEVICE_CONFIG = "phoneNumberVerification";//TODO
    public static final String COMMAND_SET_DEVICE_CONFIG = "phoneNumberVerification";//TODO
    public static final String COMMAND_GET_DEVICE_PM_CONFIG = "phoneNumberVerification";
    public static final String COMMAND_SET_DEVICE_PM_CONFIG = "phoneNumberVerification";
    public static final String COMMAND_GET_DEVICE_PMS = "phoneNumberVerification";
    public static final String COMMAND_SET_DEVICE_PMS = "phoneNumberVerification";
    public static final String COMMAND_GET_INSURANCE = "phoneNumberVerification";
    public static final String COMMAND_SET_INSURANCE = "phoneNumberVerification";
    public static final String COMMAND_GET_TICKETS = "phoneNumberVerification";
    public static final String COMMAND_GET_TICKET_MESSAGES = "phoneNumberVerification";
    public static final String COMMAND_CREATE_TICKET = "phoneNumberVerification";
    public static final String COMMAND_SEND_MESSAGE = "phoneNumberVerification";
    public static final String COMMAND_DEVICE_SMS_CREDIT = "phoneNumberVerification";
    public static final String COMMAND_GET_DEVICE_PAYMENTS = "phoneNumberVerification";
    public static final String COMMAND_GET_DEVICE_DUE_PAYMENTS = "phoneNumberVerification";
    public static final String COMMAND_GET_DEVICE_DUE_PMS = "phoneNumberVerification";
    public static final String COMMAND_GET_EVENTS = "phoneNumberVerification";
    public static final String COMMAND_SEND_COMMAND = "phoneNumberVerification";
    public static final String COMMAND_GET_COMMAND = "phoneNumberVerification";

    public CommandHelper() {
    }

    public static CommandHelper getInstance() {
        if (instance == null) {
            instance = new CommandHelper();
        }
        return instance;
    }

    public void sendCommand(Command command) {
//        SMSController.getInstance(ApplicationLoader.context).sendSms(command);
    }

    public String sendSocketCommand(String command, Object... params) {
        String strParams = "";
        for (int i = 0; i < params.length; i++) {
            strParams += ", \"" + (String) params[i] + "\" : " + "\"" + (String) params[i + 1] + "\"";
            i++;
        }

        String finalStr = "\"command\" : " + command + strParams;

        return finalStr;
    }

    public Map<String, Object> decodeCommand(String response) {
        Map<String, Object> decodeData = new HashMap<>();
        response = response.replace("Date: ", "Date:");
        response = response.replace("Time: ", "Time:");
        String[] dataStrings = response.split(" ");
        for (String data :
                dataStrings) {
            String key = data.split(":")[0];
            String value = data.split(":")[1];

            decodeData.put(key, value);
        }

        return decodeData;
    }
}
