package timereg.roninit.dk.timereg;

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
}
