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
    private String startTime;
    private String endTime;
    private String breakTime;

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
        this.taskId = trim(taskId);
    }

    private String trim(String val) {
        return val != null ? val.trim() : null;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = trim(company);
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public String getTaskNumberAndName() {
        if(getTaskNumber()!=null && !"".equals(getTaskNumber()))
            return String.format("%s-%s", getTaskNumber(), getTaskName());
        else
            return getTaskName();
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = trim(taskNumber);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = trim(taskName);
    }

    public String getHours() {
        return hours;
    }

    public BigDecimal getHoursAsBigDecimal() {
        return new BigDecimal(hours.replaceAll(":", "."));
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getAdditionInfomation() {
        return additionInfomation;
    }

    public void setAdditionInfomation(String additionInfomation) {
        this.additionInfomation = trim(additionInfomation);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = trim(date);
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = trim(submitDate);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    @Override
    public String toString() {
        return getTaskName() +" : "+getHours() +" timer";
    }
}
