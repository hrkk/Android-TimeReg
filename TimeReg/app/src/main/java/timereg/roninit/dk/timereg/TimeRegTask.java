package timereg.roninit.dk.timereg;

/**
 * Created by kasper on 05/01/2016.
 */
public class TimeRegTask {
    private String id;
    private String taskNumber;
    private String taskName;
    private double hours;
    private String additionInfomation;
    private String date;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public String getAdditionInfomation() {
        return additionInfomation;
    }

    public void setAdditionInfomation(String additionInfomation) {
        this.additionInfomation = additionInfomation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return getTaskNumber() + " "+getTaskName() +" : "+getHours() +" timer";
    }
}
