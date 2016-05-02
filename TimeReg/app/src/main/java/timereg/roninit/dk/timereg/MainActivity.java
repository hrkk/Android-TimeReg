package timereg.roninit.dk.timereg;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;

/*
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
*/
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// /Users/kasper/Library/Android/sdk/platform-tools
// ./adb shell
// chmod 777 storage/Download/
public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivity_TAG";
    private ArrayAdapter<TimeRegTask> arrayAdapter;
     private List<TimeRegTask> taskList;
     private Calendar selectedDate;
     String seletedDateAsStr;
     private ListView listView;

    private AlarmManager manager;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.title_activity_main);

  //      getWindow().getDecorView().getRootView();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                EditText date = (EditText) findViewById(R.id.date);
                String dateAsString = date.getText().toString();

                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, dateAsString);
                startActivity(intent);
            }
        });


        if (getIntent().getExtras() != null) {
             seletedDateAsStr = getIntent().getExtras().getString(DetailActivity.EXTRA_SELECTED_DATE);
        }

        if (seletedDateAsStr == null) {
            seletedDateAsStr = DateUtil.getFormattedDate(Calendar.getInstance());
        }

        EditText date = (EditText) findViewById(R.id.date);
        setDate(date, seletedDateAsStr);


        setValues(seletedDateAsStr);

        verifyStoragePermissions(this);

        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                new Intent(this, AlarmReceiver.class),
                0) != null);




      //  manager.cancel(pendingIntent);


       // if(alarmUp) {
       //    Toast.makeText(this, "alarm is UP - dont set a new one", Toast.LENGTH_SHORT).show();
      //      Toast.makeText(this, "alarm is UP - dont set a new one", Toast.LENGTH_SHORT).show();
      //      Log.d(LOG_TAG, "alarm is UP - dont set a new one");
      // } else {
            // Retrieve a PendingIntent that will perform a broadcast

            //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


            // http://blog.masconsult.eu/blog/2014/01/17/scheduling-alarms/
            manager.setRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, pendingIntent);
            //int interval = 10000;
             //manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            Log.d(LOG_TAG, "Setting alarm");
         //   Toast.makeText(this, "Setting alarm", Toast.LENGTH_SHORT).show();
        //}
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        }


        if (id == R.id.action_overview) {
            Intent intent = new Intent(this, OverviewActivity.class);
            intent.putExtra(OverviewActivityFragment.OW_TYPE, OverviewActivityFragment.OW_TYPE_WEEKLY);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void viewPdf(File myFile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private void setDate(TextView editTextDate, String date) {
        editTextDate.setText(date + " ("+DateUtil.getOnlyDayOfWeek(date)+")");
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setFocusable(false);
        editTextDate.setFocusableInTouchMode(false);
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean activ) {
                if (activ) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                }

            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayAdapter.notifyDataSetChanged();
            }
        });

        Log.d("TAG", "List Frag Resumed");

    }

    public void previousDate(View view) {
        Log.d(LOG_TAG, "previousDate called");
        EditText date = (EditText) findViewById(R.id.date);
        String dateAsStr = date.getText().toString();
        DateUtil.createDate(dateAsStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.createDate(dateAsStr));
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String previousDateAsStr = DateUtil.getFormattedDate(cal);
        date.setText(previousDateAsStr + " ("+DateUtil.getOnlyDayOfWeek(previousDateAsStr)+")");
        setValues(previousDateAsStr);
    }

    public void nextDate(View view) {
        Log.d(LOG_TAG, "nextDate called");
        EditText date = (EditText) findViewById(R.id.date);
        String dateAsStr = date.getText().toString();
        DateUtil.createDate(dateAsStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.createDate(dateAsStr));
        cal.add(Calendar.DAY_OF_YEAR, +1);
        String nextDateAsStr = DateUtil.getFormattedDate(cal);
        date.setText(nextDateAsStr + " ("+DateUtil.getOnlyDayOfWeek(nextDateAsStr)+")");
        setValues(nextDateAsStr);
    }

    private void setValues(String dateAsStr) {
        MySQLiteHelper db = new MySQLiteHelper(this);
        db.getAllTimeReg();
        taskList =  db.getAllTimeRegByDate(dateAsStr);


        TextView dayTotalHours =  (TextView) findViewById(R.id.total_hours);
        dayTotalHours.setText("" + Util.getDayTotalHours(taskList) + " timer");


        listView = (ListView) findViewById(R.id.listView);
        // sets op task list
        arrayAdapter
                = new ArrayAdapter<TimeRegTask>(
                this,
                android.R.layout.simple_list_item_1,
                taskList);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Item at " + position + " clicked with id " + id);
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                EditText date = (EditText) findViewById(R.id.date);
                String dateAsString = date.getText().toString();
                intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, dateAsString);
                TimeRegTask seletedTimeRegTask = taskList.get(position);
                intent.putExtra(DetailActivity.EXTRA_TASK_ID, seletedTimeRegTask.getId());
                startActivity(intent);
            }
        });
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        MainActivity mainActivity;

        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            Log.d("TAG", "onAttach -> activity " + activity.getClass());
            mainActivity = (MainActivity) activity;

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            Calendar c = Calendar.getInstance();
            EditText date = (EditText) getActivity().findViewById(R.id.date);
            if (!"".equals(date.getText().toString())) {
                Date date1 = DateUtil.createDate(date.getText().toString());
                c.setTime(date1);
            }

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

            if ( Build.VERSION.SDK_INT >= 21)
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);

            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.d("TAG", "Date " + year + "/" + monthOfYear + "/" + dayOfMonth + " clicked");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DATE, dayOfMonth);
            String dateAsStr = DateUtil.getFormattedDate(cal);
            EditText date = (EditText) getActivity().findViewById(R.id.date);
            date.setText(dateAsStr +" ("+DateUtil.getOnlyDayOfWeek(dateAsStr)+")");
            //selectedDate = cal;
            Log.d("TAG", "selectedDate= " + dateAsStr);
            MySQLiteHelper db = new MySQLiteHelper(getActivity());
            List<TimeRegTask> tasks =  db.getAllTimeRegByDate(dateAsStr);
            /*
            if(tasks==null) {
                tasks = new ArrayList<TimeRegTask>();
                Globals.getInstance().getTaskMap().put(dateAsStr, tasks);
            }
            */

            //arrayAdapter.notifyDataSetChanged();

          //  mainActivity.dummyChange(null);


            TextView dayTotalHours =  (TextView) getActivity().findViewById(R.id.total_hours);
            dayTotalHours.setText("" + Util.getDayTotalHours(tasks) + " timer");

            ListView listView = (ListView)  getActivity().findViewById(R.id.listView);

            ArrayAdapter<TimeRegTask> arrayAdapter
                    = new ArrayAdapter<TimeRegTask>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    tasks);

         //   mainActivity.getArrayAdapter().clear();
         //   mainActivity.getArrayAdapter().notifyDataSetChanged();
            listView.setAdapter(arrayAdapter);

            final List<TimeRegTask> finalTasks = tasks;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("TAG", "Item at " + position + " clicked with id " + id);
                    Intent intent = new Intent(mainActivity, DetailActivity.class);
                    EditText date = (EditText) mainActivity.findViewById(R.id.date);
                    String dateAsString = date.getText().toString();
                    intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, dateAsString );
                    intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, dateAsString);
                    TimeRegTask seletedTimeRegTask = finalTasks.get(position);
                    intent.putExtra(DetailActivity.EXTRA_TASK_ID, seletedTimeRegTask.getId());
                    mainActivity.startActivity(intent);
                }
            });
        }
    }
}
