package timereg.roninit.dk.timereg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kasper on 25/01/2016.
 */
public class RequestObject {
    public String startDate;
    public String endDate;
    public String name;
    public String totalHours;
    public List<SaveTask> taskList    = new ArrayList<>();
}
