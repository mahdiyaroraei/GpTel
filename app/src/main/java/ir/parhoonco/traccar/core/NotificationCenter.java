package ir.parhoonco.traccar.core;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import ir.parhoonco.traccar.R;

/**
 * Created by Parhoon on 7/24/2016.
 */
public class NotificationCenter {
    private Context context;
    
    private static NotificationCenter instance;

    public static NotificationCenter getInstance(Context context) {
        if (instance == null){
            instance = new NotificationCenter(context);
        }
        return instance;
    }
    
    public NotificationCenter(Context context){
        this.context = context;
    }
    
    public void sendNotification(String title , String text , int smallIcon){
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder n = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(smallIcon)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context.getApplicationContext());

        notificationManager.notify(0, n.build());
    }
}
