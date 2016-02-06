package timereg.roninit.dk.timereg;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kasper on 06/02/2016.
 */
public class Util {

    public static String getPeriodeStartDate(String dateAsStr) {
        Calendar cal = getPeriodeEndDateAsCal(dateAsStr);
        cal.add(Calendar.DAY_OF_YEAR, -7);
        return DateUtil.getFormattedDate(cal);
    }

    public static String getPeriodeEndDate(String dateAsStr) {

        Calendar cal = getPeriodeEndDateAsCal(dateAsStr);

        return DateUtil.getFormattedDate(cal);
    }

    public static double getDayTotalHours(List<TimeRegTask> taskList) {
        double sum=0.0;
        if (taskList != null) {
            for(TimeRegTask task : taskList) {
                sum += task.getHoursAsBigDecimal().doubleValue();
            }
        }
        return sum;
    }

    public static Calendar getPeriodeEndDateAsCal(String dateAsStr) {
        Date date = DateUtil.createDate(dateAsStr);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        // 1 meaning Monday and 7 meaning Sunday
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) -1;
        if (dayOfWeek == 0)
            dayOfWeek = 7;

        // finder ud af hvor mange der skal tilføjes før vi er på søndag
        while(dayOfWeek != 7) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
            dayOfWeek= cal.get(Calendar.DAY_OF_WEEK) -1;
            if (dayOfWeek == 0)
                dayOfWeek = 7;
        }
        return cal;
    }
}
