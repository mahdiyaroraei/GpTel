package ir.parhoonco.traccar.core;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Command;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.DeviceConfig;
import ir.parhoonco.traccar.ui.LaunchActivity;
import ir.parhoonco.traccar.ui.fragment.CarFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Parhoon on 7/24/2016.
 */
public class ApplicationLoader {
    //Creating a broadcast receiver for gcm registration
    /*private static BroadcastReceiver mRegistrationBroadcastReceiver;*/
    public static Context context;
    public static boolean isInternetAccess = false;
    public static Retrofit retrofit;
    public static TraccarAPI api;

    public static void init() {

        AndroidGraphicFactory.createInstance(((Activity) context).getApplication());

        isInternetAvailable();

        FontsOverride.setDefaultFont(context, "DEFAULT", "BMitra.ttf");
        FontsOverride.setDefaultFont(context, "MONOSPACE", "BMitra.ttf");


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://148.251.245.14:8082/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        String username = SharedPreferenceHelper.getSharedPreferenceString(context , "user_id" , "guest");
        String password = SharedPreferenceHelper.getSharedPreferenceString(context , "api_key" , "hckrUVT5FhYdTMLw");
        api = ServiceGenerator.createService(TraccarAPI.class, username , password , context);

        /*//Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    Toast.makeText(context.getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(context.getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context.getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(context.getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, context.getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(context.getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(context, GCMRegistrationIntentService.class);
            context.startService(itent);
        }*/

        try {
            sendLocalDataToServer();
        }catch (Exception e){}
    }

    public static void refreshAPI(){
        String username = SharedPreferenceHelper.getSharedPreferenceString(context , "user_id" , "guest");
        String password = SharedPreferenceHelper.getSharedPreferenceString(context , "api_key" , "hckrUVT5FhYdTMLw");
        api = ServiceGenerator.createService(TraccarAPI.class, username , password , context);
    }

    private static void sendLocalDataToServer() {
        String defaultImei = SharedPreferenceHelper.getSharedPreferenceString(context, "default_device_imei", null);
        if (isInternetAccess && defaultImei != null) {
            Device defaultDevice = Device.find(Device.class, "imei = ?", defaultImei).get(0);
            final DeviceConfig config = defaultDevice.getDeviceConfig();
            if (config != null) {
                if (config.getStatus() == null || config.getStatus().equals("success_local")) {

                    Call<Empty> call = ApplicationLoader.api.setDeviceConfig(config.getImei(), config.isProtectionmode(), config.isAutooff(),
                            Double.parseDouble(config.getMinvoltage()), config.getMaxspeed(), config.isAutogeofence(), config.getMinangle(), config.getMinperiod()
                            , config.getSendperiod(), config.getMindistance());
                    call.enqueue(new Callback<Empty>() {
                        @Override
                        public void onResponse(Call<Empty> call, Response<Empty> response) {
                            if (response.code() == 204) {
                                config.setStatus("success");
                                config.save();
                            }
                        }

                        @Override
                        public void onFailure(Call<Empty> call, Throwable t) {

                        }
                    });
                }

                List<Command> commands = Command.find(Command.class, "status = ?", "success_local");
                for (final Command command :
                        commands) {
                    Call<Empty> call = ApplicationLoader.api.addCommand(defaultImei, command.getType(), false, command.getAttributes());
                    call.enqueue(new Callback<Empty>() {
                        @Override
                        public void onResponse(Call<Empty> call, Response<Empty> response) {
                            if (response.code() == 204) {
                                command.setStatus("success");
                                command.save();
                            }
                        }

                        @Override
                        public void onFailure(Call<Empty> call, Throwable t) {

                        }
                    });
                }
            }
        }
    }

    public static void isInternetAvailable() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                InetAddress ipAddr = null; //You can replace it with your name
                try {
                    ipAddr = InetAddress.getByName("148.251.245.14");
                    isInternetAccess = !ipAddr.equals("");
                    if (!isInternetAccess) {
                        CarFragment.refreshCache();
                        ((LaunchActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((LaunchActivity) context).showOfflineLayout();
                            }
                        });
                    } else {
                        ((LaunchActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((LaunchActivity) context).hideOfflineLayout();
                            }
                        });
                    }
                } catch (UnknownHostException e) {
                    isInternetAccess = false;
                    e.printStackTrace();
                    CarFragment.refreshCache();
                    ((LaunchActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((LaunchActivity) context).showOfflineLayout();
                        }
                    });
                }
            }
        };
        thread.start();
    }

    public static void resume() {
        Log.w("MainActivity", "onResume");
        /*LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));*/
    }

    public static void pause() {
        Log.w("MainActivity", "onPause");
        /*LocalBroadcastManager.getInstance(context).unregisterReceiver(mRegistrationBroadcastReceiver);*/
    }
}
