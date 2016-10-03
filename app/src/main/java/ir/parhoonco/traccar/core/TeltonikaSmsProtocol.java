package ir.parhoonco.traccar.core;

import android.content.Context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Command;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.Position;
import ir.parhoonco.traccar.ui.fragment.CarFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeltonikaSmsProtocol {

    // SERVER COMMANDS
    public static final String TYPE_POWEROFF = "poweroff";
    public static final String TYPE_POWERON = "poweron";
    public static final String TYPE_SETSMSUSERNAME = "setsmsusername";
    public static final String TYPE_SETSMSPASSWORD = "setsmspassword";

    // CLIENTS COMMANDS
    public static final String TYPE_GETGPS = "getgps";
    public static final String TYPE_GETMINANGLE = "getminangle";
    public static final String TYPE_SETMINANGLE = "setminangle";
    public static final String TYPE_GETMINPERIOD = "getminperiod";
    public static final String TYPE_SETMINPERIOD = "setminperiod";
    public static final String TYPE_GETSENDPERIOD = "getsendperiod";
    public static final String TYPE_SETSENDPERIOD = "setsendperiod";
    public static final String TYPE_GETMINDISTANCE = "getmindistance";
    public static final String TYPE_SETMINDISTANCE = "setmindistance";
    public static final String TYPE_GETMINVOLTAGE = "getminvoltage";
    public static final String TYPE_SETMINVOLTAGE = "setminvoltage";
    public static final String TYPE_GETMAXSPEED = "getmaxspeed";
    public static final String TYPE_SETMAXSPEED = "setmaxspeed";
    public static final String TYPE_GETTHEFTALARM = "gettheftalarm";
    public static final String TYPE_SETTHEFTALARM = "settheftalarm";
    public static final String TYPE_GETGEOFENCE = "getgeofence";
    public static final String TYPE_SETGEOFENCE = "setgeofence";
    public static final String TYPE_GETGEOFENCELATITUDE = "getgeofencelatitude";
    public static final String TYPE_SETGEOFENCELATITUDE = "setgeofencelatitude";
    public static final String TYPE_GETGEOFENCELONGITUDE = "getgeofencelongitude";
    public static final String TYPE_SETGEOFENCELONGITUDE = "setgeofencelongitude";
    public static final String TYPE_GETGEOFENCERADIUS = "getgeofenceradius";
    public static final String TYPE_SETGEOFENCERADIUS = "setgeofenceradius";
    public static final String TYPE_GETAUTOGEOFENCE = "getautogeofence";
    public static final String TYPE_SETAUTOGEOFENCE = "setautogeofence";
    public static final String TYPE_GETMASTER = "getmaster";
    public static final String TYPE_SETMASTER = "setmaster";

    private static List<String> validCommands;

    public TeltonikaSmsProtocol() {
    }

    public static void checkCommand(String command) throws Exception {
        if (validCommands == null) {
            Field[] fields = TeltonikaSmsProtocol.class.getFields();
            validCommands = new ArrayList<>();
            for (Field field : fields) {
                if (field.getName().startsWith("TYPE_")) {
                    validCommands.add(field.get(String.class).toString());
                }
            }
        }
        if (!validCommands.contains(command)) {
            throw new Exception("INVALID_TYPE");
        }
    }

    public static boolean sendCommand(Device device, String type, String value, Context context) throws Exception {

//        String up = Secret.getUP(device.getImei());
        String up = "  ";
        int val;
        List<String> commands = new ArrayList<>();
        switch (type) {
            case TYPE_POWEROFF:
                commands.add(up + "setdigout 10 0 0");
                break;

            case TYPE_POWERON:
                commands.add(up + "setdigout 00 0 0");
                break;

            case TYPE_SETSMSUSERNAME:
                commands.add("  " + "setparam 1252 " + up.substring(0, 4));
                break;

            case TYPE_SETSMSPASSWORD:
                commands.add("  " + "setparam 1253 " + up.substring(6, 10));
                break;

            case TYPE_GETGPS:
                commands.add(up + "getgps");
                break;

            case TYPE_GETMINANGLE:
                commands.add(up + "getparam 1552");
                break;

            case TYPE_SETMINANGLE:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 180) {
                    commands.add(up + "setparam 1552 " + value);
                }
                break;

            case TYPE_GETMINPERIOD:
                commands.add(up + "getparam 1550");
                break;

            case TYPE_SETMINPERIOD:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 2592000) {
                    commands.add(up + "setparam 1540 " + value);
                    commands.add(up + "setparam 1550 " + value);
                }
                break;

            case TYPE_GETSENDPERIOD:
                commands.add(up + "getparam 1554");
                break;

            case TYPE_SETSENDPERIOD:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 2592000) {
                    commands.add(up + "setparam 1544 " + value);
                    commands.add(up + "setparam 1554 " + value);
                }
                break;

            case TYPE_GETMINDISTANCE:
                commands.add(up + "getparam 1551");
                break;

            case TYPE_SETMINDISTANCE:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 65535) {
                    commands.add(up + "setparam 1551 " + value);
                }
                break;

            case TYPE_GETMINVOLTAGE:
                commands.add(up + "getparam 1383");
                break;

            case TYPE_SETMINVOLTAGE:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 30000) {
                    commands.add(up + "setparam 1383 " + value);
                }
                break;

            case TYPE_GETMAXSPEED:
                commands.add(up + "getparam 1605");
                break;

            case TYPE_SETMAXSPEED:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 260) {
                    commands.add(up + "setparam 1605 " + value);
                }
                break;

            case TYPE_GETTHEFTALARM:
                commands.add(up + "getparam 1320");
                break;

            case TYPE_SETTHEFTALARM:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 1) {
                    commands.add(up + "setparam 1320 " + value);
                }
                break;

            case TYPE_GETGEOFENCE:
                commands.add(up + "getparam 1032");
                break;

            case TYPE_SETGEOFENCE:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 1) {
                    commands.add(up + "setparam 1032 " + (val == 0 ? "0" : "3"));
                }
                break;

            case TYPE_GETGEOFENCELATITUDE:
                commands.add(up + "getparam 1034");
                break;

            case TYPE_SETGEOFENCELATITUDE:
                val = Integer.parseInt(value);
                if (val >= -90 && val <= 90) {
                    commands.add(up + "setparam 1034 " + value);
                }
                break;

            case TYPE_GETGEOFENCELONGITUDE:
                commands.add(up + "getparam 1033");
                break;

            case TYPE_SETGEOFENCELONGITUDE:
                val = Integer.parseInt(value);
                if (val >= -180 && val <= 180) {
                    commands.add(up + "setparam 1033 " + value);
                }
                break;

            case TYPE_GETGEOFENCERADIUS:
                commands.add(up + "getparam 1035");
                break;

            case TYPE_SETGEOFENCERADIUS:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 1000000) {
                    commands.add(up + "setparam 1035 " + value);
                }
                break;

            case TYPE_GETAUTOGEOFENCE:
                commands.add(up + "getparam 1101");
                break;

            case TYPE_SETAUTOGEOFENCE:
                val = Integer.parseInt(value);
                if (val >= 0 && val <= 1) {
                    commands.add(up + "setparam 1101 " + value);
                }
                break;

            case TYPE_GETMASTER:
                commands.add(up + "getparam 150");
                break;

            case TYPE_SETMASTER:
                if (value.matches("^9\\d{9}$")) {
                    commands.add(up + "setparam 150 " + "98" + value);
                }
                break;

            default:
                throw new Exception("INVALID_COMMAND");
        }
        if (commands.isEmpty()) {
            throw new Exception("INVALID_VALUE");
        }
        boolean result = true;
        for (String command : commands) {
            SMSController.getInstance(context).sendSms(command);
        }
        return result;
    }

    public static void checkMessage(String message, Context context) {
        String defaultImei = SharedPreferenceHelper.getSharedPreferenceString(context, "default_device_imei", null);
        Device device = Device.find(Device.class, "imei = ?", defaultImei).get(0);
        final Command command = new Command();
        command.setDevice(device);
        command.setStatus("success");
        command.setTime(System.currentTimeMillis());

        if (message.contains("GPS:")) {
            int s_lat = message.indexOf("Lat:") + 4;
            int e_lat = message.indexOf(" Long:");
            double lat = Double.valueOf(message.substring(s_lat, e_lat));

            int s_lon = message.indexOf("Long:") + 5;
            int e_lon = message.indexOf(" Alt:");
            double lon = Double.valueOf(message.substring(s_lon, e_lon));

            int s_date = message.indexOf("Date: ") + 6;
            int e_date = message.indexOf(" Time:");
            String date = message.substring(s_date, e_date);

            int s_time = message.indexOf("Time: ") + 6;
            int e_time = message.length();
            String time_str = message.substring(s_time, e_time);

            int year = Integer.parseInt(date.split("/")[0]);
            int month = Integer.parseInt(date.split("/")[1]) - 1;
            int day = Integer.parseInt(date.split("/")[2]);
            int hour = Integer.parseInt(time_str.split(":")[0]);
            int min = Integer.parseInt(time_str.split(":")[1]);
            int sec = Integer.parseInt(time_str.split(":")[2]);
            Calendar calendar = new GregorianCalendar(year, month, day, hour, min, sec);
            long time = calendar.getTimeInMillis() / 1000;

            Position position = new Position();
            position.setLat(lat);
            position.setLongtitude(lon);
            position.setFixtime(time);
            position.save();
            device.setPosition(position);
            device.save();

            if (CarFragment.defaultDevice != null) {
                CarFragment.defaultDevice.setPosition(position);
            }

            command.setType("getgps");
        } else if (message.contains("Param ID:")) {
            int param_id = Integer.parseInt(message.substring(message.indexOf("Param ID:") + 9, message.indexOf("New")).trim());
            String value = message.split("New Val:")[1];
            switch (param_id) {
                case 1252:
                    command.setType(TYPE_SETSMSUSERNAME);
                    break;

                case 1253:
                    command.setType(TYPE_SETSMSPASSWORD);
                    break;

                case 1552:
                    command.setType(TYPE_SETMINANGLE);
                    break;

                case 1550:
                    command.setType(TYPE_SETMINPERIOD);
                    break;

                case 1554:
                    command.setType(TYPE_SETSENDPERIOD);
                    break;

                case 1551:
                    command.setType(TYPE_SETMINDISTANCE);
                    break;

                case 1383:
                    command.setType(TYPE_SETMINVOLTAGE);
                    break;

                case 1605:
                    command.setType(TYPE_SETMAXSPEED);
                    break;

                case 1320:
                    command.setType(TYPE_SETTHEFTALARM);
                    break;

                case 1032:
                    command.setType(TYPE_SETGEOFENCE);
                    break;

                case 1034:
                    command.setType(TYPE_SETGEOFENCELATITUDE);
                    break;

                case 1033:
                    command.setType(TYPE_SETGEOFENCELONGITUDE);
                    break;

                case 1035:
                    command.setType(TYPE_SETGEOFENCERADIUS);
                    break;

                case 1101:
                    command.setType(TYPE_SETAUTOGEOFENCE);
                    break;

                case 150:
                    command.setType(TYPE_SETMASTER);
                    break;
            }
            command.setAttributes(value);

        } else if (message.contains("Digital 1 Output")) {
            command.setType("setdigout");
            if (message.contains("is set to: 0")) {
                command.setAttributes(0 + "");
            } else if (message.contains("is set to: 1")) {
                command.setAttributes(1 + "");
            }
        }

        command.save();
        Call<Empty> commandCall = ApplicationLoader.api.addCommand(defaultImei, command.getType(), false, command.getAttributes());
        commandCall.enqueue(new Callback<Empty>() {
            @Override
            public void onResponse(Call<Empty> call, Response<Empty> response) {

            }

            @Override
            public void onFailure(Call<Empty> call, Throwable t) {
                command.setStatus("success_local");
                command.save();
            }
        });
    }

}
