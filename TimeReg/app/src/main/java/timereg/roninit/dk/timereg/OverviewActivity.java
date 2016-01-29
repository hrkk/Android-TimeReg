package timereg.roninit.dk.timereg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.Date;

import timereg.roninit.dk.timereg.R;

public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ow_submit) {
            View rootView = this.findViewById(android.R.id.content);
            SubmitTimeTask task = new SubmitTimeTask(rootView);
            task.execute();

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




        } else {
            Toast.makeText(getApplicationContext(), "SD card not found", Toast.LENGTH_LONG).show();
        }

    }

    private class SubmitTimeTask extends AsyncTask<String, Void, String> {

        private View rootView;
        private String timeStamp;

        public SubmitTimeTask(View rootView) {
            this.rootView = rootView;
            Date date = new Date() ;

            timeStamp =   DateUtil.getFormattedDate(new Date());
        }

        @Override
        protected String doInBackground(String... params) {
            // TO BE DONE
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // changes state
            // TODO
            TextView textView = (TextView) rootView.findViewById(R.id.ow_submitStatus);
            textView.setText("Godkent "+timeStamp);

        }
    }


}
