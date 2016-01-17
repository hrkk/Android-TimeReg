package timereg.roninit.dk.timereg;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    Calendar selectedDate;
    List<TimeRegTask> taskList;
    ArrayAdapter<TimeRegTask> arrayAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //   setDate(date, DateUtil.getFormattedDate(selectedDate));
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        EditText date = (EditText) rootView.findViewById(R.id.date);
//
//        if (selectedDate == null) {
//            selectedDate = Calendar.getInstance();
//        }
//
//        setDate(date, DateUtil.getFormattedDate(selectedDate));
//
//             taskList = Globals.getInstance().getTaskMap().get(DateUtil.getFormattedDate(selectedDate));
//             ListView listView = (ListView) rootView.findViewById(R.id.listView);
//        //  sets op task list
//        arrayAdapter
//                = new ArrayAdapter<TimeRegTask>(
//                rootView.getContext(),
//                android.R.layout.simple_list_item_1,
//                taskList);
//
//        listView.setAdapter(arrayAdapter);
        return rootView;
    }
}
