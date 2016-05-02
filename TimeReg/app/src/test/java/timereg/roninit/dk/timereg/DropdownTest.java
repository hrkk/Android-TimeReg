package timereg.roninit.dk.timereg;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Created by kasper on 19/02/2016.
 */
public class DropdownTest {

    @Test
    public void testDropdown() {
        System.out.printf("testDropdown");

        List<String> list = Util.buildPeriodeDropdownList();

        for(String e: list)
            System.out.println(e);
        assertThat(true, is(true));

    }

    @Test
    public void testJoda() {

        DateTime today = new DateTime();
        DateTime begin = new DateTime(today.year().get(), today.monthOfYear().get(), today.dayOfMonth().get(), 8, 0,0,0);

        DateTime end = new DateTime(today.year().get(), today.monthOfYear().get(), today.dayOfMonth().get(), 15, 30,0,0);


        int lunchBreak = 30;

        System.out.println("begin ="+begin);
        System.out.println("end =" + end);

        end =end.minusMinutes(lunchBreak);
        Duration dur = new Duration(begin, end);

        System.out.println("hours ="+dur.getStandardHours());
        System.out.println("minutes =" + dur.getStandardMinutes());

        int hours = Hours.hoursBetween(begin, end).getHours();
        int minutes = Minutes.minutesBetween(begin, end).getMinutes();
        System.out.println(hours);
       // System.out.println(minutes);


        System.out.printf("fullHours "+hours);
        System.out.printf(" minutes "+(minutes - (hours*60)));

        assertThat(true, is(true));

    }
}
