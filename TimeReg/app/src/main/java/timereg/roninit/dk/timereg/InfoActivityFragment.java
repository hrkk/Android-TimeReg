package timereg.roninit.dk.timereg;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class InfoActivityFragment extends Fragment {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String IdNumber = "idKey";
    public static final String Email = "emailKey";
    SharedPreferences sharedpreferences;
    EditText fullName;
    EditText infoIdNumber;
    EditText email;

    public InfoActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String prefName =  sharedpreferences.getString(Name, "");
        String prefIdNumber =  sharedpreferences.getString(IdNumber, "");
        String prefEmail =  sharedpreferences.getString(Email, "");


        fullName=(EditText)rootView.findViewById(R.id.fullName);
        infoIdNumber =(EditText) rootView.findViewById(R.id.info_id_number);
        email =(EditText) rootView.findViewById(R.id.info_email);


        fullName.setText(prefName);
        infoIdNumber.setText(prefIdNumber);
        email.setText(prefEmail);
        Button b1=(Button)rootView.findViewById(R.id.button);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n  = fullName.getText().toString();
                String ph  = infoIdNumber.getText().toString();
                String e  = email.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Name, n);
                editor.putString(IdNumber, ph);
                editor.putString(Email, e);
                editor.commit();
                Toast.makeText(getContext(), "Thanks", Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }
}
