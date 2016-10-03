package ir.parhoonco.traccar.core;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.SparseArray;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import ir.parhoonco.traccar.core.model.GPSElement;
import ir.parhoonco.traccar.core.model.api.Command;

/**
 * Created by Parhoon on 7/24/2016.
 */
public class SMSController {
    public static final int didReceivedNewSMS = 1;
    public static final int didSentSMS = 2;
    public static final int didDeliveredSMS = 3;
    public static short SMS_PORT = 9801;
    public static String DEST_ADDRESS = "989332831460";
    public static double longitude;
    public static double latitude;
    public static long speed;
    public static Date date;
    private static SMSController instance;
    private final String login = "asd 123 ";
    BroadcastReceiver sendBroadcastReceiver = new SentReceiver();
    BroadcastReceiver deliveryBroadcastReciever = new DeliverReceiver();
    private SparseArray<ArrayList<Object>> observers = new SparseArray<>();
    private long codecId;
    private long timeStamp;
    private long elementCount;
    private long validElements;
    private long differentialCoords;
    private long longitudeDiff;
    private long latitudeDiff;
    private long imei;
    private Context context;


    public SMSController(Context context) {
        this.context = context;
    }

    public static SMSController getInstance(Context context) {
        if (instance == null) {
            instance = new SMSController(context);
        }
        return instance;
    }

    public void getSms(String address, String body) {
        Map<String, Object> response = CommandHelper.getInstance().decodeCommand(body);

        latitude = Double.parseDouble(String.valueOf(response.get("Lat")));
        longitude = Double.parseDouble(String.valueOf(response.get("Long")));
        speed = Long.parseLong(String.valueOf(response.get("Speed")));

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            date = df.parse(String.valueOf(response.get("Date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            DatabaseHelper.getHelper(context).getGpsElementsDao().create(new GPSElement(longitude, latitude, speed, date));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Object> objects = observers.get(didReceivedNewSMS);
        if (objects != null && !objects.isEmpty()) {
            for (int a = 0; a < objects.size(); a++) {
                Object obj = objects.get(a);
                ((NotificationCenterDelegate) obj).didReceivedNotification(didReceivedNewSMS, address, body);
            }
        }
    }

    public void getDataSms(byte[] data) {
        decode(data);
    }

    public void sendSms(String command) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        Intent sentIntent = new Intent(SENT);

        Intent deliveredIntent = new Intent(DELIVERED);

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, deliveredIntent, 0);

        context.registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));

        context.registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(DEST_ADDRESS, null, command , sentPI, deliveredPI);
    }

    public void encode() {

    }

    public void decode(byte[] msg) {
    }


    public void addObserver(Object observer, int id) {

        ArrayList<Object> objects = observers.get(id);
        if (objects == null) {
            observers.put(id, (objects = new ArrayList<>()));
        }
        if (objects.contains(observer)) {
            return;
        }
        objects.add(observer);
    }

    public void removeObserver(Object observer, int id) {
        ArrayList<Object> objects = observers.get(id);
        if (objects != null) {
            objects.remove(observer);
        }
    }

    public void removeListeners() {
        try {
            context.unregisterReceiver(sendBroadcastReceiver);
            context.unregisterReceiver(deliveryBroadcastReciever);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public interface NotificationCenterDelegate {
        void didReceivedNotification(int id, Object... params);
    }

    class DeliverReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            boolean delivered = false;
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "Delivered",
                            Toast.LENGTH_SHORT).show();
                    delivered = true;
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "Not delivered",
                            Toast.LENGTH_SHORT).show();
                    delivered = false;
                    break;
            }
            ArrayList<Object> objects = observers.get(didDeliveredSMS);
            if (objects != null && !objects.isEmpty()) {
                for (int a = 0; a < objects.size(); a++) {
                    Object obj = objects.get(a);
                    ((NotificationCenterDelegate) obj).didReceivedNotification(didDeliveredSMS, delivered);
                }
            }

        }
    }

    class SentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            boolean sent = false;
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "Sent", Toast.LENGTH_SHORT)
                            .show();
                    sent = true;
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "Generic failure",
                            Toast.LENGTH_SHORT).show();
                    sent = false;
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, "No service",
                            Toast.LENGTH_SHORT).show();
                    sent = false;
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT)
                            .show();
                    sent = false;
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, "Radio off",
                            Toast.LENGTH_SHORT).show();
                    sent = false;
                    break;
            }
            ArrayList<Object> objects = observers.get(didSentSMS);
            if (objects != null && !objects.isEmpty()) {
                for (int a = 0; a < objects.size(); a++) {
                    Object obj = objects.get(a);
                    ((NotificationCenterDelegate) obj).didReceivedNotification(didSentSMS, sent);
                }
            }

        }
    }
}
