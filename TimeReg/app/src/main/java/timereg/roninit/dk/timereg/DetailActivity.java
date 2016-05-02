package timereg.roninit.dk.timereg;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.pinball83.maskededittext.MaskedEditText;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_DATE = "EXTRA_SELECTED_DATE";
    public static final java.lang.String EXTRA_TASK_ID = "EXTRA_TASK_ID";
    String seletedDateAsStr;
    int taskId;
    private MySQLiteHelper db;

    private TimePicker timePicker1;
    private Button btnChangeTime;

    private int hour;
    private int minute;

    static final int TIME_DIALOG_ID = 999;

    public static final String LOG_TAG = "DetailActivity_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new MySQLiteHelper(this);
        if (getIntent().getExtras() != null) {
            seletedDateAsStr = getIntent().getExtras().getString(EXTRA_SELECTED_DATE);
            seletedDateAsStr = seletedDateAsStr.substring(0, 10);
            taskId = getIntent().getExtras().getInt(EXTRA_TASK_ID, -1);
            getSupportActionBar().setTitle(getIntent().getExtras().getString(EXTRA_SELECTED_DATE));
        }

        AutoCompleteTextView company = (AutoCompleteTextView) findViewById(R.id.company);

        AutoCompleteTextView taskName = (AutoCompleteTextView) findViewById(R.id.taskName);

        List<TimeRegTask> allTimeReg = db.getAllTimeReg();

        String[] companies = Util.getAutoCompleteCompany(allTimeReg).toArray(new String[0]);
      //  String[] taskNumbers = Util.getAutoCompleteTaskNumber(allTimeReg).toArray(new String[0]);
        String[] taskNames = Util.getAutoCompleteTaskName(allTimeReg).toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, companies);
       // ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskNumbers);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskNames);

        // set adapter for the auto complete fields
        company.setAdapter(adapter);

        taskName.setAdapter(adapter2);
        // specify the minimum type of characters before drop-down list is shown
        company.setThreshold(1);

        taskName.setThreshold(1);

        EditText startTime = (EditText) findViewById(R.id.taskStartHours);
        EditText endTime = (EditText) findViewById(R.id.taskEndHours);
        EditText breakTime =(EditText) findViewById(R.id.taskBreakTime);

        startTime.setText("08:00");
        endTime.setText("16:00");
        breakTime.setText("00:30");

        setOnFocusChangeListener(startTime);
        setOnFocusChangeListener(endTime);
        setOnFocusChangeListener(breakTime);



        // TODO look up in DB
        if (-1 != taskId) {
            MySQLiteHelper db = new MySQLiteHelper(this);
            TimeRegTask timeRegTask = db.getTimeReg(taskId);

            //Log.d(LOG_TAG, "DB taskId "+name.getId() + " "+name.getTaskNumber() +" " +name.getTaskName());
            company.setText(timeRegTask.getCompany());

            taskName.setText(timeRegTask.getTaskName());

            EditText taskDecription = (EditText) findViewById(R.id.taskDescription);
            taskDecription.setText(timeRegTask.getAdditionInfomation());

            startTime.setText(timeRegTask.getStartTime());
            endTime.setText(timeRegTask.getEndTime());

            breakTime.setText(timeRegTask.getBreakTime());

            TextView taskHours = (TextView) findViewById(R.id.taskHours);
            taskHours.setText(timeRegTask.getHours());
        }
    }

    private static int setMinuteRound(int minute, int hour) {
        // runder ned eller op
        if(minute <=7)
            minute=0;
        else if (minute<=15)
            minute=15;
        else if(minute<=22)
            minute = 15;
        else if(minute<=30)
            minute = 30;
        else if(minute<=37)
            minute=30;
        else if(minute<=45)
            minute=45;
        else if(minute<=52)
            minute = 45;
        else {
            minute = 0;
            hour = hour+1;
        }
        return minute;
    }

    private static int setHourRound(int minute, int hour) {
        // runder ned eller op
        if(minute <=7)
            minute=0;
        else if (minute<=15)
            minute=15;
        else if(minute<=22)
            minute = 15;
        else if(minute<=30)
            minute = 30;
        else if(minute<=37)
            minute=30;
        else if(minute<=45)
            minute=45;
        else if(minute<=52)
            minute = 45;
        else {
            minute = 0;
            hour = hour+1;
        }
        return hour;
    }

    private void setOnFocusChangeListener(EditText editTextDate) {
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean activ) {

                    EditText startTime = (EditText)findViewById(R.id.taskStartHours);
                    EditText endTime = (EditText) findViewById(R.id.taskEndHours);
                    EditText breakTime = (EditText) findViewById(R.id.taskBreakTime);
                    TextView hours = (TextView) findViewById(R.id.taskHours);

                    updateHours(startTime, endTime, breakTime, hours);
            }
        });

        editTextDate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText startTime = (EditText)findViewById(R.id.taskStartHours);
                EditText endTime = (EditText) findViewById(R.id.taskEndHours);
                EditText breakTime = (EditText) findViewById(R.id.taskBreakTime);
                TextView hours = (TextView) findViewById(R.id.taskHours);

                updateHours(startTime, endTime, breakTime, hours);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (-1 == taskId) {
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
            EditText company = (EditText) findViewById(R.id.company);

            EditText taskName = (EditText) findViewById(R.id.taskName);
            TextView taskHours = (TextView) findViewById(R.id.taskHours);
            EditText taskDescription = (EditText) findViewById(R.id.taskDescription);
            EditText taskStartHours = (EditText) findViewById(R.id.taskStartHours);
            EditText taskEndHours = (EditText) findViewById(R.id.taskEndHours);
            EditText taskBreakTime = (EditText) findViewById(R.id.taskBreakTime);

            if (company.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Virksomhedsnavn er ikke udfyldt.", Toast.LENGTH_LONG).show();
                return true;
            }

            /*
            if (taskNumber.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Opgave nummer er ikke udfyldt.", Toast.LENGTH_LONG).show();
                return true;
            }
            */

            if (taskName.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Opgave navn er ikke udfyldt.", Toast.LENGTH_LONG).show();
                return true;
            }

            if (taskHours.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Antal timer er ikke udfyldt.", Toast.LENGTH_LONG).show();
                return true;
            }

            String taskHoursAsString = taskHours.getText().toString();
            if (taskHoursAsString != null && taskHoursAsString.length() > 0) {
                boolean modulusOk = false;
                taskHoursAsString = taskHoursAsString.replace(":", ".");
                BigDecimal taskHoursAsDecimal = new BigDecimal(taskHoursAsString);

                BigDecimal remainder = taskHoursAsDecimal.remainder(BigDecimal.ONE);
                // laver 0.15, 0.30, 0.45 om til
                BigDecimal res = new BigDecimal("0.00");

                if (remainder.equals(res) || remainder.equals(new BigDecimal("0")) ) {
                    modulusOk = true;
                } else {
                    if (new BigDecimal("0.15").equals(remainder) || new BigDecimal("0.30").equals(remainder) || new BigDecimal("0.45").equals(remainder)) {
                        modulusOk = true;
                    }
                }

                if (!modulusOk) {
                    Toast.makeText(getApplicationContext(), "Timer er ikke korrekt udfyldt. 15 min., 30 min., 45 min.", Toast.LENGTH_LONG).show();
                    return true;
                }
            }

            // 2. tilføje til global liste
            if (company != null && company.length() > 0 && taskName != null && taskHours != null && taskHours.length() > 0) {

                TimeRegTask newTimeRegTask = new TimeRegTask();
                setParams(company, taskName, taskHours, taskDescription, seletedDateAsStr, taskStartHours, taskEndHours, taskBreakTime, newTimeRegTask);

                // List<TimeRegTask> timeRegTasks = Globals.getInstance().getTaskMap().get(seletedDateAsStr);

                // taskId = index
                // hvis taskId er der ikke valgt en og den næste i listen vælges
                if (-1 == taskId) {
                    db.addTimeReg(newTimeRegTask);
                } else {
                    TimeRegTask timeRegTask = db.getTimeReg(taskId);
                    setParams(company, taskName, taskHours, taskDescription, taskStartHours, taskEndHours, taskBreakTime,timeRegTask);
                    db.updateTimeReg(timeRegTask);
                }
                //  Globals.getInstance().getTaskMap().put(seletedDateAsStr, timeRegTasks);
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, seletedDateAsStr);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_delete) {
            MySQLiteHelper db = new MySQLiteHelper(this);
            TimeRegTask timeRegTask = db.getTimeReg(taskId);
            db.deleteTimeReg(timeRegTask);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, seletedDateAsStr);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//
//    public void showTimePickerDialog(View v) {
//        TimePickerFragment newFragment = (TimePickerFragment) new TimePickerFragment();
//        newFragment.setIsEndTime(false);
//
//        newFragment.show(getSupportFragmentManager(), "timePicker");
//    }
//
//    public void showTimePickerDialogEnd(View v) {
//        TimePickerFragment newFragment = (TimePickerFragment) new TimePickerFragment();
//        newFragment.setIsEndTime(true);
//
//        newFragment.show(getSupportFragmentManager(), "timePicker");
//    }
//
//
//    public void showTimePickerDialogBreak(View v) {
//        TimePickerFragment newFragment = (TimePickerFragment) new TimePickerFragment();
//        newFragment.setIsEndTime(false);
//
//        newFragment.show(getSupportFragmentManager(), "timePicker");
//    }

//    public static class TimePickerFragment extends DialogFragment
//            implements TimePickerDialog.OnTimeSetListener {
//
//        private boolean isEndTime=false;
//
//
//        public void setIsEndTime(Boolean val) {
//            this.isEndTime = val;
//        }
//
//
//
//        @NonNull
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//          //  DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
//            // lets read aktuel time
//            int hour;
//            int minute;
//            EditText startTime = (EditText) getActivity().findViewById(R.id.taskStartHours);
//            if(isEndTime)
//                startTime = (EditText) getActivity().findViewById(R.id.taskEndHours);
//            String text = startTime.getText().toString();
//            String[] split = text.split(":");
//            if(split.length==1){
//                final Calendar c = Calendar.getInstance();
//                hour = c.get(Calendar.HOUR_OF_DAY);
//                minute = c.get(Calendar.MINUTE);
//                hour = setHourRound(minute, hour);
//                minute = setMinuteRound(minute, hour);
//            } else {
//                hour = Integer.valueOf(split[0]);
//                minute = Integer.valueOf(split[1]);
//            }
//            TimePickerDialog picker = new TimePickerDialog(getActivity(),
//                    this, hour, minute, true); // true = 24 timer
//
//      //      timePicker.setIs24HourView
//      //      picker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
//            return picker;
//            // return super.onCreateDialog(savedInstanceState);
//        }
//
////        @Override
////        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
////            Log.d(LOG_TAG, "onTimeSet hour=" + hourOfDay + " minute=" + minute);
////
////            hourOfDay = setHourRound(minute, hourOfDay);
////            minute = setMinuteRound(minute, hourOfDay);
////
////            EditText startTime = (EditText) getActivity().findViewById(R.id.taskStartHours);
////            if(isEndTime)
////                startTime = (EditText) getActivity().findViewById(R.id.taskEndHours);
////            // set current time into textview
////            startTime.setText(new StringBuilder().append(Util.pad(hourOfDay))
////                    .append(":").append(Util.pad(minute)));
////
////
////             startTime = (EditText) getActivity().findViewById(R.id.taskStartHours);
////            EditText endTime = (EditText) getActivity().findViewById(R.id.taskEndHours);
////            EditText breakTime = (EditText) getActivity().findViewById(R.id.taskBreakTime);
////            EditText hours = (EditText) getActivity().findViewById(R.id.taskHours);
////
////            updateHours(startTime, endTime, breakTime, hours);
////
////            // set current time into timepicker
////           // this.setCurrentHour(hour);
////           // timePicker1.setCurrentMinute(minute);
////
////
////        }
//
//
//
//
//    }



    private static void updateHours(EditText startTime,  EditText endTime,  EditText breakTime, TextView hours) {

        String text = startTime.getText().toString();
        String[] startSplit = text.split(":");


        String endText = endTime.getText().toString();
        String[] endTextSpilt = endText.split(":");
        if (startSplit.length != 1 && endTextSpilt.length != 1) {
            if(startSplit[0].trim().length()==2
                    && startSplit[1].trim().length()==2
                    && endTextSpilt[0].trim().length()==2
                    && endTextSpilt[1].trim().length()==2 ) {
                int startHour = Integer.valueOf(startSplit[0]);
                int startMinute = Integer.valueOf(startSplit[1]);

                int endHour = Integer.valueOf(endTextSpilt[0]);
                int endMinute = Integer.valueOf(endTextSpilt[1]);


                DateTime today = new DateTime();
                DateTime begin = new DateTime(today.year().get(), today.monthOfYear().get(), today.dayOfMonth().get(), startHour, startMinute, 0, 0);

                DateTime end = new DateTime(today.year().get(), today.monthOfYear().get(), today.dayOfMonth().get(), endHour, endMinute, 0, 0);
                if (breakTime != null && breakTime.getText() != null && breakTime.getText().length() > 0) {
                    String[] breakSplit = breakTime.getText().toString().split(":");
                    if(breakSplit.length ==2 && breakSplit[0].trim().length()==2 && breakSplit[1].trim().length()==2) {
                        end = end.minusHours(Integer.valueOf(breakSplit[0]));
                        end = end.minusMinutes(Integer.valueOf(breakSplit[1]));
                    }
                }

                int diffHours = Hours.hoursBetween(begin, end).getHours();
                int diffMinutes = Minutes.minutesBetween(begin, end).getMinutes() % 60;

/*
            if(diffMinutes ==15) {
                diffMinutes = 25;
            } else if(diffMinutes == 30) {
                diffMinutes = 50;
            } else if(diffMinutes == 45) {
                diffMinutes = 75;
            }
*/
                hours.setText(new StringBuilder().append(
                        diffHours)
                        .append(":").append(Util.pad(diffMinutes)));
            }
        }
    }


    private void setParams(EditText company, EditText taskName, TextView taskHours, EditText taskDescription, EditText taskStartHours, EditText taskEndHours, EditText taskBreakHours, TimeRegTask newTimeRegTask) {
        newTimeRegTask.setCompany(company.getText().toString());

        newTimeRegTask.setTaskName(taskName.getText().toString());
        newTimeRegTask.setHours(taskHours.getText().toString());
        newTimeRegTask.setStartTime(taskStartHours.getText().toString());
        newTimeRegTask.setEndTime(taskEndHours.getText().toString());
        newTimeRegTask.setBreakTime(taskBreakHours.getText().toString());
        if (taskDescription != null) {
            newTimeRegTask.setAdditionInfomation(taskDescription.getText().toString());
        }
    }

    private void setParams(EditText company,  EditText taskName, TextView taskHours, EditText taskDescription, String seletedDateAsStr, EditText taskStartHours, EditText taskEndHours, EditText taskBreakHours, TimeRegTask newTimeRegTask) {
        newTimeRegTask.setCompany(company.getText().toString());

        newTimeRegTask.setTaskName(taskName.getText().toString());
        newTimeRegTask.setHours(taskHours.getText().toString());
        newTimeRegTask.setDate(seletedDateAsStr);
        newTimeRegTask.setStartTime(taskStartHours.getText().toString());
        newTimeRegTask.setEndTime(taskEndHours.getText().toString());
        newTimeRegTask.setBreakTime(taskBreakHours.getText().toString());
        if (taskDescription != null) {
            newTimeRegTask.setAdditionInfomation(taskDescription.getText().toString());
        }
    }
}
