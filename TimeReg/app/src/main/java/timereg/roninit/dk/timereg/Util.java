package timereg.roninit.dk.timereg;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kasper on 06/02/2016.
 */
public class Util {

    public static List<String> buildPeriodeDropdownList() {

        List<String> res = new ArrayList<>(8);

        Calendar lastdayInWeek = getLastdayInWeek(Calendar.getInstance().getTime());

        for(int i=0; i< 8; i++) {
            String firstDayInWeekAsString = getPeriodeStartDate(DateUtil.getFormattedDate(lastdayInWeek));
            String lastDayInWeekAsString = getPeriodeEndDate(DateUtil.getFormattedDate(lastdayInWeek));

            res.add(String.format("%s - %s", firstDayInWeekAsString, lastDayInWeekAsString));
            lastdayInWeek.add(Calendar.DAY_OF_YEAR, -8);

        }
        return res;
    }

    public static String getPeriodeStartDate(String dateAsStr) {
        Calendar cal = getPeriodeEndDateAsCal(dateAsStr);
        cal.add(Calendar.DAY_OF_YEAR, -6);
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

    public static List<String> getAutoCompleteCompany( List<TimeRegTask> allTimeReg ) {
        List<String> res = new ArrayList<>();

        for (TimeRegTask e:allTimeReg) {
           if(!res.contains(e.getCompany()) && e.getCompany() != null && e.getCompany().length() > 0)
             res.add(e.getCompany());
        }

        return res;
    }

    public static List<String> getAutoCompleteTaskNumber( List<TimeRegTask> allTimeReg ) {
        List<String> res = new ArrayList<>();

        for (TimeRegTask e:allTimeReg) {
            if(!res.contains(e.getTaskNumber()) && e.getTaskNumber() != null && e.getTaskNumber().length() > 0)
                res.add(e.getTaskNumber());
        }

        return res;
    }

    public static List<String> getAutoCompleteTaskName( List<TimeRegTask> allTimeReg ) {
        List<String> res = new ArrayList<>();

        for (TimeRegTask e:allTimeReg) {
            if(!res.contains(e.getTaskName()) && e.getTaskName() != null && e.getTaskName().length() > 0)
                res.add(e.getTaskName());
        }

        return res;
    }


    public static Calendar getPeriodeEndDateAsCal(String dateAsStr) {
        Date date = DateUtil.createDate(dateAsStr);

        return getLastdayInWeek(date);
    }

    @NonNull
    private static Calendar getLastdayInWeek(Date date) {
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
