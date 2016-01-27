package timereg.roninit.dk.timereg;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        taskList =  Globals.getInstance().getTaskMap().get(seletedDateAsStr);

        TextView dayTotalHours =  (TextView) findViewById(R.id.total_hours);
        dayTotalHours.setText("" + getDayTotalHours(taskList) + " timer");

        TextView weekTotalHours = (TextView) findViewById(R.id.week_total_hours);
        weekTotalHours.setText("" + Globals.getInstance().getWeekTotal(seletedDateAsStr) +" timer");

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
                intent.putExtra(DetailActivity.EXTRA_TASK_ID, position);
                startActivity(intent);
            }
        });

        verifyStoragePermissions(this);
    }

    private static double getDayTotalHours(List<TimeRegTask> taskList) {
        double sum=0.0;
        if (taskList != null) {
            for(TimeRegTask task : taskList) {
                sum +=task.getHours();
            }
        }
        return sum;
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

        if (id == R.id.action_submit || id==R.id.action_preview) {


            try {
                createPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //popup window code here
//            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
//
//            // set the message to display
//            alertbox.setMessage("Sender ugens timer til server, som sender mail til Mølle med medarbejderens timer");
//
//            // add a neutral button to the alert box and assign a click listener
//            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//
//                // click listener on the alert box
//                public void onClick(DialogInterface arg0, int arg1) {
//                    // the button was clicked
//                    //  Globals.getInstance().reset();
//                }
//            });
//
//            // show it
//            alertbox.show();
            return true;

        }

        if (id == R.id.action_test_notification) {
            Intent intent = new Intent(this, CreateNotificationActivity.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    //http://www.codeproject.com/Articles/986574/Android-iText-Pdf-Example
    private void createPdf() throws IOException, DocumentException {



     //   Environment. getExternalStoragePublicDirectory();

//        myFile.createNewFile();

       // String filename = "myfile.pdf";

        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
           // File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
          //  File dir = new File(root.getAbsolutePath()+"/MyAppFile");
           // if(!dir.exists()){
           //     dir.mkdir();
           // }

            /*

            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "pdfdemo");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i("LOG_TAG", "Pdf Directory created" + pdfFolder.getAbsolutePath() + " ::" + pdfFolder.getPath());
            }

            File file = new File(pdfFolder, "message2.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write("hej".getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Message saved", Toast.LENGTH_LONG).show();
            */
            EditText date = (EditText) findViewById(R.id.date);

            String dateAsString = date.getText().toString();
            dateAsString = dateAsString.substring(0,10);
             ReadURLP readUrlTask = new ReadURLP();
            readUrlTask.setDate(dateAsString);
            readUrlTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "SD card not found", Toast.LENGTH_LONG).show();
        }

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


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        MainActivity mainActivity;

        @Override
        public void onAttach(Activity activity)
        {
            Log.d("TAG", "onAttach -> activity " + activity.getClass());

            mainActivity = (MainActivity) activity;
            super.onAttach(activity);
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
            List<TimeRegTask> tasks = Globals.getInstance().getTaskMap().get(dateAsStr);
            if(tasks==null) {
                tasks = new ArrayList<TimeRegTask>();
                Globals.getInstance().getTaskMap().put(dateAsStr, tasks);
            }

            //arrayAdapter.notifyDataSetChanged();

          //  mainActivity.dummyChange(null);


            TextView dayTotalHours =  (TextView) getActivity().findViewById(R.id.total_hours);
            dayTotalHours.setText("" + getDayTotalHours(tasks) + " timer");

            TextView weekTotalHours = (TextView) getActivity().findViewById(R.id.week_total_hours);
            weekTotalHours.setText("" + Globals.getInstance().getWeekTotal(dateAsStr) +" timer");

            ListView listView = (ListView)  getActivity().findViewById(R.id.listView);

            ArrayAdapter<TimeRegTask> arrayAdapter
                    = new ArrayAdapter<TimeRegTask>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    tasks);

         //   mainActivity.getArrayAdapter().clear();
         //   mainActivity.getArrayAdapter().notifyDataSetChanged();
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("TAG", "Item at " + position + " clicked with id " + id);
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    EditText date = (EditText) getActivity().findViewById(R.id.date);
                    String dateAsString = date.getText().toString();
                    intent.putExtra(DetailActivity.EXTRA_SELECTED_DATE, dateAsString );
                    intent.putExtra(DetailActivity.EXTRA_TASK_ID, position);
                    startActivity(intent);
                }
            });
        }
    }


    private class ReadURLP extends AsyncTask<String, Void, String> {

        private String seletedDateAsStr;

        public void setDate (String date) {
            this.seletedDateAsStr = date;
        }
        // public static String URL = Constants.CONTEXT_ROOT + "/LoppemarkederAdminApp/markedItem/listJSON2";
        File myFile;
        @Override
        public String doInBackground(String... urls) {

            URL url = null;
            try {


                File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "pdfdemo");
                if (!pdfFolder.exists()) {
                    pdfFolder.mkdir();
                    Log.i(LOG_TAG, "Pdf Directory created" + pdfFolder.getAbsolutePath() + " ::" + pdfFolder.getPath());
                }

                // test
                RestTemplate restTemplate = new RestTemplate();
             //   HttpMessageConverter<RequestObject> messageConverter = new HttpMessageConverter<RequestObject>();

                MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
              //  jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
               // restTemplate.getMessageConverters().add(jsonHttpMessageConverter);

                HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(stringHttpMessageConverternew);
                //String quote = restTemplate.getForObject("http://localhost:8080/timeReg/taskList", String.class);
                String quote = restTemplate.getForObject("http://www.roninit.dk:81/timeReg/taskList", String.class);
                Log.i(LOG_TAG, "taskList=" + quote.toString());

                // first we create the file

              //  RestTemplate restTemplate = new RestTemplate();
                RequestObject rq = new RequestObject();

                List<TimeRegTask> weekList = Globals.getInstance().getWeekList(seletedDateAsStr);
                if(weekList!=null)
                for(TimeRegTask e : weekList) {
                    // TODO get the tasks for the periode
                    SaveTask saveTask1 = new SaveTask();
                    saveTask1.setDate(e.getDate());
                    saveTask1.setDesciption(e.getAdditionInfomation());
                    saveTask1.setHours("" + e.getHours());
                    saveTask1.setTaskName(e.getTaskName());
                    saveTask1.setTaskNo(e.getTaskNumber());
                    rq.taskList.add(saveTask1);
                }

                HttpEntity<RequestObject> request = new HttpEntity<>(rq);
                String filename = restTemplate.postForObject("http://www.roninit.dk:81/timeReg/makePdf", request, String.class);
                Log.i(LOG_TAG, "postForObject called on server "+filename);
                // then we read it



                //Create time stamp
               // Date date = new Date() ;
               // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
               // String fileName = pdfFolder + "/"+ timeStamp + ".pdf";
                 String deviceFileName = pdfFolder + "/"+filename;
                Log.i(LOG_TAG, "fileName "+filename);

                myFile = new File(deviceFileName);
                //
                url = new URL("http://www.roninit.dk:81/timeReg/showImage?fileName="+filename);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                copyInputStreamToFile(inputStream, myFile);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "response";
        }

        @Override
        protected void onPostExecute(String result) {
            viewPdf(myFile);

        }

        private void copyInputStreamToFile( InputStream in, File file ) {
            try {
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while((len=in.read(buf))>0){
                    out.write(buf,0,len);
                }
                out.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
