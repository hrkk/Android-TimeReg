package timereg.roninit.dk.timereg;

import java.math.BigDecimal;

/**
 * Created by kasper on 05/01/2016.
 */
public class TimeRegTask {
    private int id; // db
    private String taskId;
    private String company;
    private String taskNumber;
    private String taskName;
    private String hours;
    private String additionInfomation;
    private String date;
    private String submitDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getHours() {
        return hours;
    }

    public BigDecimal getHoursAsBigDecimal() {
        return new BigDecimal(hours);
    }

    public void setHours(String hours) {
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

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    @Override
    public String toString() {
        return getTaskNumber() + " "+getTaskName() +" : "+getHours() +" timer";
    }
}
