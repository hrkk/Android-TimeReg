package timereg.roninit.dk.timereg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.GetChars;
import android.util.Log;

public class DateUtil {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String ONLY_WEEKDAY_FORMAT = "EEEEEEE";
    private static final String DATE_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public static String getFormattedDate(Calendar calendar) {
        // Locale locale = new Locale("da");
        // DateFormat f = DateFormat.getDateTimeInstance(DateFormat.SHORT,
        // DateFormat.SHORT, locale );

        // SimpleDateFormat f = new SimpleDateFormat("MMM dd yyyy");
        // f.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        return sdf.format(calendar.getTime());
    }

    public static String getOnlyDayOfWeek(String dateAsString) {
        SimpleDateFormat sdf = new SimpleDateFormat(ONLY_WEEKDAY_FORMAT);
        return sdf.format( createDate(dateAsString));
    }

    public static String getFormattedDate(Date calendar) {
        // Locale locale = new Locale("da");
        // DateFormat f = DateFormat.getDateTimeInstance(DateFormat.SHORT,
        // DateFormat.SHORT, locale );

        // SimpleDateFormat f = new SimpleDateFormat("MMM dd yyyy");
        // f.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        return sdf.format(calendar);
    }

    public static String getFormattedDate(int year, int month, int day) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, day);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.YEAR, year);
        return sdf.format(now.getTime());
    }

    private static Date createDate(int day, int month, int year, int hour,
                                   int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, hour);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDateWithoutTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, date.getDate());
        cal.set(Calendar.MONTH, date.getMonth() - 1);
        cal.set(Calendar.YEAR, date.getYear());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date createDate(String dateAsString) {
        Date temp = null;
        try {
            temp = new SimpleDateFormat(DATE_FORMAT)
                    .parse(dateAsString);
        } catch (ParseException e) {
            Log.e("" + DateUtil.class, "Can not parse date!!", e);
        }
        return temp;
    }

}
