package timereg.roninit.dk.timereg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import timereg.roninit.dk.timereg.R;

public class OverviewActivity extends AppCompatActivity {
    public static final String LOG_TAG = "OverviewActivity_TAG";
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // prepare for a progress bar dialog
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("Sender data til server ...");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

            getMenuInflater().inflate(R.menu.menu_overview, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The a
        // automatically handle clicks on the Home/U
        // as you specify a parent activity in Andro
        int id = item.getItemId();

        // lets check if info's is set
        SharedPreferences sharedpreferences = getSharedPreferences(InfoActivityFragment.MyPREFERENCES, Context.MODE_PRIVATE);

        String prefName =  sharedpreferences.getString(InfoActivityFragment.Name, "");
        String prefIdNumber =  sharedpreferences.getString(InfoActivityFragment.IdNumber, "");
        String prefEmail =  sharedpreferences.getString(InfoActivityFragment.Email, "");
        String prefServerApi = sharedpreferences.getString(InfoActivityFragment.SERVER_API_KEY, "");

        if("".equals(prefName) || "".equals(prefIdNumber) || "".equals(prefEmail) || "".equals(prefServerApi)){
            Toast.makeText(this,
                    "Felter i \"Mine oplysninger\" skal udfyldes før du kan preview pdf og godkende timer",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ow_submit) {
            View rootView = this.findViewById(android.R.id.content);
            if("".equals(prefName))
                prefName ="Not found";
            SubmitTimeTask task = new SubmitTimeTask(prefName,rootView);
            task.execute();

            return true;
        }

        if (id==R.id.action_preview) {
            createPdf();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //http://www.codeproject.com/Articles/986574/Android-iText-Pdf-Example
    private void createPdf()  {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            SharedPreferences sharedPreferences = getSharedPreferences(InfoActivityFragment.MyPREFERENCES, Context.MODE_PRIVATE);
            String prefName = sharedPreferences.getString(InfoActivityFragment.Name, "Not found");
            View rootView = this.findViewById(android.R.id.content);
            PreviewPdfTask previewTask = new PreviewPdfTask(prefName, rootView);
            previewTask.execute();

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

    //http://www.codeproject.com/Articles/986574/Android-iText-Pdf-Example
    private class PreviewPdfTask extends AsyncTask<String, Void, String> {
        File myFile;
        String seletedDateAsStr;

        String name;
        private View rootView;
        public PreviewPdfTask(String name, View rootView) {

            this.name = name;
            this.rootView = rootView;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            seletedDateAsStr = Globals.getInstance().getOverviewSeletedDate();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "timereg");
                if (!pdfFolder.exists()) {
                    pdfFolder.mkdir();
                    Log.i(LOG_TAG, "Pdf Directory created" + pdfFolder.getAbsolutePath() + " ::" + pdfFolder.getPath());
                }

                RestTemplate restTemplate = new RestTemplate();
                HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(stringHttpMessageConverternew);
                RequestObject rq = createRequestObject(seletedDateAsStr, name);


                HttpEntity<RequestObject> request = new HttpEntity<>(rq);
                String filename = restTemplate.postForObject("http://www.roninit.dk:81/timeReg/makePdf", request, String.class);
                Log.i(LOG_TAG, "postForObject called on server "+filename);
                // then we read it

                String deviceFileName = pdfFolder + "/"+filename;
                Log.i(LOG_TAG, "fileName "+filename);

                // TODO store in DB
                Globals.getInstance().setFilename(filename);

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

    private class SubmitTimeTask extends AsyncTask<String, Void, String> {

        private View rootView;
        private String timeStamp;
        private String seletedDateAsStr;
        private String name;

        public SubmitTimeTask(String name, View rootView) {
            this.name = name;
            this.rootView = rootView;
            timeStamp =   DateUtil.getFormattedDate(new Date());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            seletedDateAsStr = Globals.getInstance().getOverviewSeletedDate();
            progressBar.show();
        }

        @Override
        protected String doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();
            restTemplate.getMessageConverters().add(stringHttpMessageConverternew);
            RequestObject rq = createRequestObject(seletedDateAsStr, name);


            HttpEntity<RequestObject> request = new HttpEntity<>(rq);
            String filename = restTemplate.postForObject("http://www.roninit.dk:81/timeReg/makePdf", request, String.class);
            Log.i(LOG_TAG, "postForObject called on server "+filename);
            String quote = restTemplate.getForObject("http://www.roninit.dk:81/timeReg/submitToDropbox?fileName=" + filename, String.class);



            return "response";
        }

        @Override
        protected void onPostExecute(String result) {

            // changes status

            MySQLiteHelper db = new MySQLiteHelper(rootView.getContext());

            List<TimeRegTask> weekList = getWeekList(seletedDateAsStr);
            if(weekList!=null)
                for(TimeRegTask e : weekList) {
                    e.setSubmitDate(timeStamp);
                    // lets update all obj in DB
                    db.updateTimeReg(e);
                }

            TextView textView = (TextView) rootView.findViewById(R.id.ow_submitStatus);
            textView.setText("Godkent "+timeStamp);
            Toast.makeText(getBaseContext(), "Timer er godkendt", Toast.LENGTH_SHORT).show();
            progressBar.dismiss();
        }
    }

    @NonNull
    private RequestObject createRequestObject(String seletedDateAsStr, String name) {
        // first we create the file
        RequestObject rq = new RequestObject();
        rq.startDate = Util.getPeriodeStartDate(seletedDateAsStr);
        rq.endDate = Util.getPeriodeEndDate(seletedDateAsStr);
        rq.name = name;

        List<TimeRegTask> weekList = getWeekList(seletedDateAsStr);
        rq.totalHours = "" + Util.getDayTotalHours(weekList);

        if (weekList != null)
            for (TimeRegTask e : weekList) {
                SaveTask saveTask1 = new SaveTask();
                saveTask1.setDate(e.getDate());
                saveTask1.setDesciption(e.getAdditionInfomation());
                saveTask1.setHours("" + e.getHours());
                saveTask1.setTaskName(e.getTaskName());
                saveTask1.setTaskNo(e.getTaskNumber());
                rq.taskList.add(saveTask1);
            }
        return rq;
    }

    public List<TimeRegTask> getWeekList(String dateAsStr) {
        MySQLiteHelper db = new MySQLiteHelper(this);
        Calendar endDate = Util.getPeriodeEndDateAsCal(dateAsStr);
        // 1 meaning Monday and 7 meaning Sunday
        int dayOfWeek = endDate.get(Calendar.DAY_OF_WEEK) -1;
        if (dayOfWeek == 0)
            dayOfWeek = 7;

        List<TimeRegTask> weekList= new ArrayList<TimeRegTask>();
        while(dayOfWeek>=1) {
            String formattedDate = DateUtil.getFormattedDate(endDate);
            Log.d("getWeekList", "Working on date " + formattedDate);
            weekList.addAll(db.getAllTimeRegByDate(formattedDate));

            endDate.add(Calendar.DAY_OF_YEAR, -1);
            dayOfWeek= endDate.get(Calendar.DAY_OF_WEEK) -1;
            if (dayOfWeek == 0) {
                break;
            }
        }
        Collections.reverse(weekList);
        return weekList;
    }
}
