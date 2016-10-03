package ir.parhoonco.traccar.core;

import android.util.Log;

import org.mapsforge.core.model.LatLong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.parhoonco.traccar.BuildConfig;
import ir.parhoonco.traccar.core.model.TicketItem;

/**
 * Created by Parhoon on 7/24/2016.
 */
public class SocketController {
    private static SocketController instance = null;
    private final String TAG = "Socket";
    private Socket socket = null;
    private InputStream inStream = null;
    private OutputStream outStream = null;
    private HashMap<String, ArrayList<Object>> observers = new HashMap<>();

    public SocketController() {

    }

    public static SocketController getInstance() {
        if (instance == null) {
            instance = new SocketController();
        }
        return instance;
    }

    public void connect() {
        try {
            socket = new Socket(String.valueOf(BuildConfig.SERVER_ADDRESS), BuildConfig.PORT);
            Log.d(TAG, "Connected");
            inStream = socket.getInputStream();
            outStream = socket.getOutputStream();
            createReadThread();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void createReadThread() {
        Thread readThread = new Thread() {
            public void run() {
                while (socket.isConnected()) {

                    try {
                        byte[] readBuffer = new byte[1024];
                        int num = inStream.read(readBuffer);

                        if (num > 0) {
                            byte[] arrayBytes = new byte[num];
                            System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
                            String receivedMessage = new String(arrayBytes, "UTF-8");

                            System.out.println("Received message :" + receivedMessage);
                        }
                    } catch (IOException i) {
                        i.printStackTrace();
                    }

                }
            }
        };
        readThread.setPriority(Thread.MAX_PRIORITY);
        readThread.start();
    }

    public void write(final String typedMessage) {
        final Thread writeThread = new Thread() {
            public void run() {
                try {
                    if (typedMessage != null && typedMessage.length() > 0) {
                        outStream.write(typedMessage.getBytes("UTF-8"));
                    }
                    this.interrupt();
                } catch (IOException i) {
                    i.printStackTrace();
                    this.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        writeThread.setPriority(Thread.MAX_PRIORITY);
        writeThread.start();
    }

    public void phoneNumberVerification(String phone, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_PHONE_VERIFY, "phone", phone));
        addObserver(response, CommandHelper.COMMAND_PHONE_VERIFY);

        response.didReceiveResponse(true, new HashMap<String, Object>());
    }

    public void forgroundLogin(String phone, String vercode, String clientId, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_FORGROUND_LOGIN
                , "phone", phone
                , "vercode", vercode
                , "client_type", "Android"
                , "client_id", clientId
        ));
        addObserver(response, CommandHelper.COMMAND_FORGROUND_LOGIN);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("api_key", "a4s45hg324d6liu324fdf248qwd92h3df");
        response.didReceiveResponse(true, params);
    }

    public void backgroundLogin(String phone, String apiKey, String clientId, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_BACKGROUND_LOGIN
                , "phone", phone
                , "api_key", apiKey
                , "client_type", "Android"
                , "client_id", clientId
        ));
        addObserver(response, CommandHelper.COMMAND_BACKGROUND_LOGIN);

        ArrayList<String[]> devices = new ArrayList<>();
        devices.add(new String[]{"156554", "BMW"});
        devices.add(new String[]{"865213", "Audi"});
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("devices", devices);
        response.didReceiveResponse(true, params);
    }

    public void changeUserName(String name, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_CHANGE_USERNAME
                , "name", name
        ));
        addObserver(response, CommandHelper.COMMAND_CHANGE_USERNAME);

        response.didReceiveResponse(true, new HashMap<String, Object>());
    }

    public void getDeviceInfo(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_INFO
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_INFO);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("temperature", "7");
        params.put("odometer", "120");
        params.put("voltage", "7.2");
        params.put("motion", true);
        params.put("protection_mode", true);
        params.put("status", "on");
        params.put("last_update", "36000");
        params.put("engine_status", "off");
        params.put("stealing_mode", true);
        params.put("door_status", "open");
        params.put("light_status", "on");

        response.didReceiveResponse(true, params);
    }

    public void getDeviceGroup(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_GROUP
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_GROUP);

        HashMap<String, Object> params = new HashMap<String, Object>();

        ArrayList<Map> group = new ArrayList<Map>();

        String[] names = {"Ali", "Mahdiyar", "Hamed"};
        int[] ids = {72342, 10474, 72123};

        for (int i = 0; i < 3; i++) {
            HashMap<String, Object> user = new HashMap<String, Object>();
            user.put("name", names[i]);
            user.put("id", ids[i]);

            group.add(user);
        }
        params.put("group", group);

        response.didReceiveResponse(true, params);
    }

    public void getDevicePosition(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_POSITION
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_POSITION);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("latitude", 36.1825);
        params.put("longitude", 59.3217);

        response.didReceiveResponse(true, params);
    }

    public void getDevicePositionAttr(String imei, String positionId, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_POSITION
                , "imei", imei
                , "position_id", positionId
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_POSITION);
    }


    public void getDevicePositionHistory(String imei, long startTime, long endTime, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_POSITION_HISTORY
                , "imei", imei
                , "start_time", String.valueOf(startTime)
                , "end_time", String.valueOf(endTime)
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_POSITION_HISTORY);

        ArrayList<LatLong> latLongs = new ArrayList<>();
        latLongs.add(new LatLong(36.1825, 59.3217));
        latLongs.add(new LatLong(36.1848, 59.3154));
        latLongs.add(new LatLong(36.1859, 59.3159));
        latLongs.add(new LatLong(36.1909, 59.3139));

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("positions", latLongs);

        response.didReceiveResponse(true, params);
    }

    public void getDeviceConfig(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_CONFIG
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_CONFIG);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("data_send_mode", "GPRS");
        params.put("protection_mode", false);
        params.put("stealing_mode", false);
        params.put("auto_off", false);

        response.didReceiveResponse(true, params);
    }

    public void setDeviceConfig(String imei, Map config, CallBackResponse response) {//TODO
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_SET_DEVICE_CONFIG
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_SET_DEVICE_CONFIG);

        response.didReceiveResponse(true, new HashMap<String, Object>());
    }

    public void getDevicePMConfig(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_PM_CONFIG
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_PM_CONFIG);
    }

    public void setDevicePMConfig(String imei, Map config, CallBackResponse response) {//TODO
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_SET_DEVICE_PM_CONFIG
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_SET_DEVICE_PM_CONFIG);
    }

    public void getDevicePMs(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_PMS
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_PMS);
    }

    public void setDevicePMs(String imei, Map pms, CallBackResponse response) {//TODO
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_SET_DEVICE_PMS
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_SET_DEVICE_PMS);
    }

    public void getInsurance(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_INSURANCE
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_INSURANCE);
    }

    public void setInsurance(String imei, String insurance, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_SET_INSURANCE
                , "imei", imei
                , "insurance", insurance
        ));
        addObserver(response, CommandHelper.COMMAND_SET_INSURANCE);
    }

    public void getTickets(String offset, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_TICKETS
                , "offset", offset
        ));
        addObserver(response, CommandHelper.COMMAND_GET_TICKETS);

        Map<String, Object> params = new HashMap<>();

        ArrayList<TicketItem> tickets = new ArrayList<>();

        int[] ids = {5656, 6856, 4589, 5456, 86456, 68446, 684465, 988, 8989, 66, 989, 98, 998, 8};
        String[] subjects = {"hdsfgsdfdfs ", "asudtyytdg q", "adjiqwtg fvb  uewi e"
                , "hdsfgsdfdfs ", "asudtyytdg q", "adjiqwtg fvb  uewi e"
                , "hdsfgsdfdfs ", "asudtyytdg q", "adjiqwtg fvb  uewi e"
                , "hdsfgsdfdfs ", "asudtyytdg q", "adjiqwtg fvb  uewi e"};
        String[] lastMessage = {"hdsfgsdfdfs ", "asudtyytdg q", "adjiqwtg fvb  uewi e"
                , "hdsfgsdfdfs ", "asudtyytdg q", "adjiqwtg fvb  uewi e"
                , "hdsfgsdfdfs ", "asudtyytdg q", "adjiqwtg fvb  uewi e"
                , "hdsfgsdfdfs ", "asudtyytdg q", "adjiqwtg fvb  uewi e"};

        for (int i = 0; i < 10; i++) {
            TicketItem item = new TicketItem();
            item.setId(ids[i]);
            item.setTitle(subjects[i]);
            item.setDesc(lastMessage[i]);
            tickets.add(item);
        }

        params.put("tickets", tickets);

        response.didReceiveResponse(true, params);
    }

    public void getTicketMessages(String ticketId, String offset, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_TICKET_MESSAGES
                , "ticket_id", ticketId
                , "offset", offset
        ));
        Map params = new HashMap();

        ArrayList<Map> messages = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            Map<String, Object> message = new HashMap<>();
            if (i % 2 == 0)
                message.put("own", true);
            else
                message.put("own", false);
            message.put("message" , "Salam");
            message.put("ticket_id" , 445456);
            message.put("time" , System.currentTimeMillis() - i * 2 * 60 * 1000);
            messages.add(message);
        }
        addObserver(response, CommandHelper.COMMAND_GET_TICKET_MESSAGES);

        params.put("messages" , messages);
        response.didReceiveResponse(true , params);
    }

    public void createTicket(String subject, String body, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_TICKET_MESSAGES
                , "subject", subject
                , "body", body
        ));
        addObserver(response, CommandHelper.COMMAND_GET_TICKET_MESSAGES);
    }


    public void sendMessage(String ticketId, String body, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_TICKET_MESSAGES
                , "ticket_id", ticketId
                , "body", body
        ));
        addObserver(response, CommandHelper.COMMAND_GET_TICKET_MESSAGES);
    }


    public void getDeviceSMSCredit(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_DEVICE_SMS_CREDIT
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_DEVICE_SMS_CREDIT);
    }

    public void getDevicePayments(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_PAYMENTS
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_PAYMENTS);
    }

    public void getDeviceDuePayments(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_DUE_PAYMENTS
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_DUE_PAYMENTS);
    }

    public void getDeviceDuePMs(String imei, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_DEVICE_DUE_PMS
                , "imei", imei
        ));
        addObserver(response, CommandHelper.COMMAND_GET_DEVICE_DUE_PMS);
    }


    public void getEvents(String imei, String startTime, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_EVENTS
                , "imei", imei
                , "start_time", startTime
        ));
        addObserver(response, CommandHelper.COMMAND_GET_EVENTS);
    }

    public void getCommands(String imei, String startTime, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_GET_COMMAND
                , "imei", imei
                , "start_time", startTime
        ));
        addObserver(response, CommandHelper.COMMAND_GET_COMMAND);
    }

    public void setCommands(String imei, Map commands, CallBackResponse response) {
        write(CommandHelper.getInstance().sendSocketCommand(CommandHelper.COMMAND_SEND_COMMAND
                , "imei", imei
                , "commands", commands
        ));
        addObserver(response, CommandHelper.COMMAND_SEND_COMMAND);
    }

    public void addObserver(Object observer, String id) {

        ArrayList<Object> objects = observers.get(id);
        if (objects == null) {
            observers.put(id, (objects = new ArrayList<>()));
        }
        if (objects.contains(observer)) {
            return;
        }
        objects.add(observer);
    }

    public void removeObserver(Object observer, String id) {
        ArrayList<Object> objects = observers.get(id);
        if (objects != null) {
            objects.remove(observer);
        }
    }

    public interface CallBackResponse {
        void didReceiveResponse(boolean success, Map<String, Object> params);
    }
}
