package timereg.roninit.dk.timereg;

/**
 * Created by kasper on 25/01/2016.
 */
public class SaveTask {

    private String date;
    private String taskNo;
    private String taskName;
    private String hours;
    private String desciption;

    public SaveTask() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    @Override
    public String toString() {
        return "SaveTask{" +
                "date='" + date + '\'' +
                ", taskNo='" + taskNo + '\'' +
                ", taskName='" + taskName + '\'' +
                ", hours='" + hours + '\'' +
                ", desciption='" + desciption + '\'' +
                '}';
    }
}
