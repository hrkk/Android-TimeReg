package timereg.roninit.dk.timereg;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kasper on 05/01/2016.
 */
public class Globals {
    public static final String TAG = "GLOBALS_TAG";
    private static Globals instance;

    // Global variable
    private int data;

    public String type;

    public String getType(){
        if(type==null)
            return "WEEKLY";
        return type;
    }

    public String filename;

    //private List<TimeRegTask> tasks;
    private List<List<TimeRegTask>> taskListList;

    private Map<String, List<TimeRegTask>> taskMap;
    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setData(int d){
        this.data=d;
    }
    public int getData(){
        return this.data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String overviewSeletedDate;

    //    public void setTasks(List<TimeRegTask> tasks) {
//        this.tasks = tasks;
//    }

//    public List<TimeRegTask> getTasks() {
//        return tasks;
//    }

    public double getWeekTotal() {
        double weekTotal = 0;
        for(List<TimeRegTask> listE : taskListList){
            for(TimeRegTask e : listE){
                weekTotal += e.getHoursAsBigDecimal().doubleValue();
            }
        }

        return weekTotal;
    }




    private double getDayTotalHours(String dateAsStr) {
        List<TimeRegTask> taskList = taskMap.get(dateAsStr);
        double sum=0.0;
        if (taskList != null) {
            for(TimeRegTask task : taskList) {
                sum +=task.getHoursAsBigDecimal().doubleValue();
            }
        }
        return sum;
    }
    public List<List<TimeRegTask>> getTaskListList() {
        return taskListList;
    }

    public void setTaskListList(List<List<TimeRegTask>> taskListList) {
        this.taskListList = taskListList;
    }


    public void reset() {
        for(List<TimeRegTask> listE : taskListList) {
            listE.clear();
        }
    }


    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
            List<TimeRegTask> stringCollection = new ArrayList<>();
            TimeRegTask task1 = new TimeRegTask();
            task1.setId(1);
            task1.setTaskNumber("I1654");
            task1.setTaskName("Demo Projekt 1");
            task1.setHours("2.5");
            task1.setAdditionInfomation("5 meter 1/2 tomme rør");

            stringCollection.add(task1);

            TimeRegTask task2 = new TimeRegTask();
            task2.setId(2);
            task2.setTaskNumber("U1518");
            task2.setTaskName("Demo Projekt 2");
            task2.setHours("4.5");
            task2.setAdditionInfomation("11 meter jern!!!");

            stringCollection.add(task2);

            List<List<TimeRegTask>> taskListList = new ArrayList<>();

            taskListList.add(0, new ArrayList<TimeRegTask>());
            taskListList.add(1, stringCollection);
            // add empty lists
            taskListList.add(2, new ArrayList<TimeRegTask>());
            taskListList.add(3, new ArrayList<TimeRegTask>());
            taskListList.add(4, new ArrayList<TimeRegTask>());
            taskListList.add(5, new ArrayList<TimeRegTask>());
            taskListList.add(6, new ArrayList<TimeRegTask>());
            taskListList.add(7, new ArrayList<TimeRegTask>());

            //   instance.setTasks(stringCollection);

            Map<String, List<TimeRegTask>> taskMap = new HashMap<>();


            String formattedToDate = DateUtil.getFormattedDate(Calendar.getInstance());

            taskMap.put(formattedToDate, stringCollection);
            instance.setTaskMap(taskMap);

            instance.setTaskListList(taskListList);
        }
        return instance;
    }

    public void setTaskMap(Map<String, List<TimeRegTask>> taskMap) {
        this.taskMap = taskMap;
    }

    public Map<String, List<TimeRegTask>> getTaskMap() {
        return taskMap;
    }

    public void setOverviewSeletedDate(String dateAsStr) {
        overviewSeletedDate = dateAsStr;
    }

    public String getOverviewSeletedDate() {
        return overviewSeletedDate;
    }
}
