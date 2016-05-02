package timereg.roninit.dk.timereg;

import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kasper on 06/02/2016.
 */
public class Util {

    public static List<String> buildPeriodeDropdownList() {

        List<String> res = new ArrayList<>(8);

        Calendar lastdayInWeek = getLastdayInWeek(Calendar.getInstance().getTime());
        lastdayInWeek.set(Calendar.HOUR, 0);

        for(int i=0; i< 9; i++) {
            String firstDayInWeekAsString = getPeriodeStartDate(DateUtil.getFormattedDate(lastdayInWeek));
            String lastDayInWeekAsString = getPeriodeEndDate(DateUtil.getFormattedDate(lastdayInWeek));

            res.add(String.format("%s - %s", firstDayInWeekAsString, lastDayInWeekAsString));
            lastdayInWeek.add(Calendar.DAY_OF_YEAR, -7);
            System.out.println("lastdayInWeekDate "+DateUtil.getFormattedDate(lastdayInWeek));

        }
        return res;
    }

    public static List<String> buildMontlyPeriodeDropdownList() {


        List<String> res = new ArrayList<>(12);

        DateTime firstDayInMonth = new DateTime().dayOfMonth().withMinimumValue();
        DateTime lastDayInMonth = new DateTime().dayOfMonth().withMaximumValue();

        for(int i=0; i< 12; i++) {

            String firstDayInMonthAsStr = new LocalDate(firstDayInMonth).toString(DateUtil.DATE_FORMAT);
            String lastDayInMonthAsStr = new LocalDate(lastDayInMonth).toString(DateUtil.DATE_FORMAT);
            res.add(String.format("%s - %s", firstDayInMonthAsStr, lastDayInMonthAsStr));

            firstDayInMonth = firstDayInMonth.minusMonths(1);
            lastDayInMonth = lastDayInMonth.minusMonths(1);
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

    public static String getDayTotalHours(List<TimeRegTask> taskList) {
        // double sum=0.0;
        Hours sumH = Hours.hours(0);
        Minutes sumM = Minutes.minutes(0);
        if (taskList != null) {
            for (TimeRegTask task : taskList) {
                // sum += task.getHoursAsBigDecimal().doubleValue();
                String strHours = task.getHours().replace(".", ":");
                String[] split = strHours.split(":");
                String h = split[0];
                if (split.length > 1) {
                    String m = split[1];
                    sumM = sumM.plus(Integer.valueOf(m));
                }
                sumH = sumH.plus(Integer.valueOf(h));

            }
        }

        if (sumM.getMinutes() != 0) {
            int ho = sumM.getMinutes() / 60;
            sumH = sumH.plus(ho);
        }
        int min = sumM.getMinutes() % 60;
        int hours = sumH.getHours();

        return new StringBuilder().append(
                hours)
                .append(":").append(Util.pad(min)).toString();

    }

    public static Map<String, List<TimeRegTask>> splitTimeRegs(List<TimeRegTask> taskList) {
        // lets spilt into maps pr task

        Map<String, List<TimeRegTask>> map = new HashMap<String, List<TimeRegTask>>();
        if (taskList != null) {
            for (TimeRegTask e : taskList) {

                if(map.containsKey(e.getTaskNumberAndName())) {
                    List<TimeRegTask> timeRegTasks = map.get(e.getTaskNumberAndName());
                    timeRegTasks.add(e);
                } else {
                    List<TimeRegTask> newList = new ArrayList<>();
                    newList.add(e);
                    map.put(e.getTaskNumberAndName(), newList);
                }
            }
        }

      return map;


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

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
