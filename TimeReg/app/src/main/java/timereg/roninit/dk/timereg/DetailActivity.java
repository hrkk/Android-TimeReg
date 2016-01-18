package timereg.roninit.dk.timereg;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_DATE =  "EXTRA_SELECTED_DATE";
    public static final java.lang.String EXTRA_TASK_ID = "EXTRA_TASK_ID";
    String seletedDateAsStr;
    int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().getExtras() != null) {
            seletedDateAsStr = getIntent().getExtras().getString(EXTRA_SELECTED_DATE);
            seletedDateAsStr = seletedDateAsStr.substring(0,10);
            taskId = getIntent().getExtras().getInt(EXTRA_TASK_ID, -1);
        }


        if(-1!=taskId) {
            List<TimeRegTask> timeRegTasks = Globals.getInstance().getTaskMap().get(seletedDateAsStr);
            TimeRegTask timeRegTask = timeRegTasks.get(taskId);

            EditText taskNumber = (EditText) findViewById(R.id.taskNumber);
            taskNumber.setText(timeRegTask.getTaskNumber());
            EditText taskName = (EditText) findViewById(R.id.taskName);
            taskName.setText(timeRegTask.getTaskName());

            EditText taskHours = (EditText) findViewById(R.id.taskHours);
            taskHours.setText("" + timeRegTask.getHours());

            EditText taskDecription = (EditText) findViewById(R.id.taskDescription);
            taskDecription.setText(timeRegTask.getAdditionInfomation());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (-1==taskId) {
            getMenuInflater().inflate(R.menu.menu_detail_new, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_detail, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The a
        // automatically handle clicks on the Home/U
        // as you specify a parent activity in Andro
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            // 1. læs input text
            EditText taskNumber = (EditText) findViewById(R.id.taskNumber);
            EditText taskName = (EditText) findViewById(R.id.taskName);
            EditText taskHours = (EditText) findViewById(R.id.taskHours);
            EditText taskDescription = (EditText) findViewById(R.id.taskDescription);

            // 2. tilføje til global liste
            if (taskName != null && taskHours != null && taskHours.length() > 0 && taskNumber != null) {
                TimeRegTask newTimeRegTask = new TimeRegTask();
                setParams(taskNumber, taskName, taskHours, taskDescription, newTimeRegTask);


                List<TimeRegTask> timeRegTasks = Globals.getInstance().getTaskMap().get(seletedDateAsStr);

                if (-1==taskId) {
                    timeRegTasks.add(newTimeRegTask);
                } else {
                    TimeRegTask timeRegTask = timeRegTasks.get(taskId);

                    setParams(taskNumber, taskName, taskHours, taskDescription, timeRegTask);
                    timeRegTasks.get(taskId).setAdditionInfomation(timeRegTask.getAdditionInfomation());
                    timeRegTasks.get(taskId).setHours(timeRegTask.getHours());
                    timeRegTasks.get(taskId).setTaskName(timeRegTask.getTaskName());
                    timeRegTasks.get(taskId).setTaskNumber(timeRegTask.getTaskNumber());
                }

                Globals.getInstance().getTaskMap().put(seletedDateAsStr, timeRegTasks);
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, seletedDateAsStr);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_delete) {
            List<TimeRegTask> timeRegTasks = Globals.getInstance().getTaskMap().get(seletedDateAsStr);

            timeRegTasks.remove(taskId);
            Globals.getInstance().getTaskMap().put(seletedDateAsStr, timeRegTasks);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, seletedDateAsStr);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setParams(EditText taskNumber, EditText taskName, EditText taskHours, EditText taskDescription, TimeRegTask newTimeRegTask) {
        newTimeRegTask.setTaskNumber(taskNumber.getText().toString());
        newTimeRegTask.setTaskName(taskName.getText().toString());
        newTimeRegTask.setHours(Double.parseDouble(taskHours.getText().toString()));
        if (taskDescription != null) {
            newTimeRegTask.setAdditionInfomation(taskDescription.getText().toString());
        }
    }
}


