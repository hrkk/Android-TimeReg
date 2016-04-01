package timereg.roninit.dk.timereg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kasper on 28/01/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        //Resources r = getResources();
        MySQLiteHelper db  = new MySQLiteHelper(context);

        // dd-MM-yyyy
        Date skaeringsDato = DateUtil.createDate("01-02-2016");
        List<String> alarmDates =  Util.buildPeriodeDropdownList();
        alarmDates.remove(0); // remove current periode..
        String alarmDatePeriode=null;
        for(String e: alarmDates) {
            String mondayAsStr = e.substring(0, 10);
            String dayAsStr = mondayAsStr;
            boolean isPeriodeApproved = false;
            for(int i=0;i<7;i++) {
                List<TimeRegTask> allTimeRegByDate = db.getAllTimeRegByDate(dayAsStr);
                // lets check if that day is approved
                if (!allTimeRegByDate.isEmpty() && allTimeRegByDate.get(0).getSubmitDate()!=null) {
                    isPeriodeApproved = true;
                }
                // add one day
                Date date = DateUtil.createDate(dayAsStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                dayAsStr=DateUtil.getFormattedDate(calendar);

            }
            // hvis perioden er før d. 01-02-2016 så skal vi ikke breake
            Date date = DateUtil.createDate(e.substring(0, 10));
            // after =
            if (skaeringsDato.after(date)) {
                Log.d("AlarmReceiver", "date="+DateUtil.getFormattedDate(date) +" skaeringsDato="+ DateUtil.getFormattedDate(skaeringsDato));
                //   Toast.makeText(context, "AlarmReceiver -> ingen tidsreg fundet i periode " + DateUtil.getFormattedDate(date), Toast.LENGTH_SHORT).show();
                //       break;
                isPeriodeApproved = true;
            }

            if(!isPeriodeApproved) {
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
