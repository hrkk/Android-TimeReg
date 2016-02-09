package timereg.roninit.dk.timereg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.util.List;

/**
 * Created by kasper on 28/01/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        Toast.makeText(context, "AlarmReceiver -> I'm running waked up", Toast.LENGTH_SHORT).show();
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        //Resources r = getResources();
        MySQLiteHelper db  = new MySQLiteHelper(context);

        List<String> alarmDates =  Util.buildPeriodeDropdownList();
        alarmDates.remove(0); // remove current periode..
        String alarmDatePeriode=null;
        for(String e: alarmDates) {
            List<TimeRegTask> allTimeRegByDate = db.getAllTimeRegByDate(e.substring(10));
            if(allTimeRegByDate.isEmpty()){
                alarmDatePeriode = e;
                break;
            }
        }

        if(alarmDatePeriode!=null) {
            Notification notification = new NotificationCompat.Builder(context)
                    .setTicker("Tidsreg mangler!")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setContentTitle("Tidsreg mangler!")
                    .setContentText(alarmDatePeriode +" er ikke godkendt!!!")
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            notificationManager.notify(0, notification);
        }
    }
}
