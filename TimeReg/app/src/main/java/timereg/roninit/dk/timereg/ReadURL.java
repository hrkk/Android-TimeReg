package timereg.roninit.dk.timereg;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kasper on 21/01/2016.
 */
public class ReadURL extends AsyncTask<String, Void, String> {

   // public static String URL = Constants.CONTEXT_ROOT + "/LoppemarkederAdminApp/markedItem/listJSON2";

    @Override
    public String doInBackground(String... urls) {

        URL url = null;
        try {
            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "pdfdemo");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i("LOG_TAG", "Pdf Directory created" + pdfFolder.getAbsolutePath() + " ::" + pdfFolder.getPath());
            }



            //Create time stamp
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

            File myFile = new File(pdfFolder + "/"+ timeStamp + ".pdf");
            url = new URL("http://www.roninit.dk:81/timeReg/showImage");
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