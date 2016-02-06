package timereg.roninit.dk.timereg;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import timereg.roninit.dk.timereg.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class OverviewActivityFragment extends Fragment {

    private Spinner spinner2;

    public OverviewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        addItemsOnSpinner2(rootView);
        addListenerOnSpinnerItemSelection(rootView);
        return rootView;
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2( View rootView ) {

        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        List<String> list = Util.buildPeriodeDropdownList();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }


    public void addListenerOnSpinnerItemSelection(final View rootView ) {
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              /*
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
*/
                String dateAsStr = parent.getItemAtPosition(position).toString().substring(0, 10);
                Globals.getInstance().setOverviewSeletedDate(dateAsStr);
                Log.d("spinner", "selectedDate= " + dateAsStr);
                TextView weekTotalHours = (TextView) rootView.findViewById(R.id.ow_week_total_hours);
                List<TimeRegTask> tasks = getWeekList(dateAsStr, rootView);
                weekTotalHours.setText("" + Util.getDayTotalHours(tasks) + " timer");


                TextView textView = (TextView) rootView.findViewById(R.id.ow_submitStatus);
                if (!tasks.isEmpty())
                    textView.setText(tasks.get(0).getSubmitDate() != null ? " Godkent " + tasks.get(0).getSubmitDate() : "Ikke godkendt");

                //    ListView listView = (ListView)  getActivity().findViewById(R.id.ow_listView);

//                ArrayAdapter<TimeRegTask> arrayAdapter
//                        = new ArrayAdapter<TimeRegTask>(
//                        getActivity(),
//                        android.R.layout.simple_list_item_1,
//                        tasks);

                //   mainActivity.getArrayAdapter().clear();
                //   mainActivity.getArrayAdapter().notifyDataSetChanged();
                //       listView.setAdapter(arrayAdapter);

                //   init(rootView);

                TableLayout table = (TableLayout) rootView.findViewById(R.id.lastExpensesTable);

                // lets clear all first
                table.removeViews(1, table.getChildCount() - 1);
                table.setStretchAllColumns(true);


                for (TimeRegTask e : tasks) {

                    TableRow tr = new TableRow(getContext());

                    TextView c1 = new TextView(getContext());
                    c1.setText(e.getDate());
                    c1.setBackgroundColor(Color.WHITE);


                    TextView c2 = new TextView(getContext());
                    c2.setText(e.getCompany());
                    c2.setBackgroundColor(Color.WHITE);

                    TextView c3 = new TextView(getContext());
                    c3.setText(e.getTaskNumber() +"-"+e.getTaskName());
                    c3.setBackgroundColor(Color.WHITE);

                    TextView c4 = new TextView(getContext());
                    c4.setText("" + e.getHours());
                    c4.setBackgroundColor(Color.WHITE);

                    tr.addView(c1);
                    tr.addView(c2);
                    tr.addView(c3);
                    tr.addView(c4);

                    table.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                    // new row for description
                    TableRow tr2 = new TableRow(getContext());

                    TextView c12 = new TextView(getContext());
                    c12.setBackgroundColor(Color.WHITE);
                    c12.setText("Beskrivelse:");
                    tr2.addView(c12); // empty
                    TextView c5 = new TextView(getContext());
                    c5.setText(e.getAdditionInfomation()!=null ? e.getAdditionInfomation() : "Ikke angivet");
                    c5.setBackgroundColor(Color.WHITE);
                    tr2.addView(c5);
                    TableRow.LayoutParams params = (TableRow.LayoutParams) c5.getLayoutParams();
                    params.span = 3; //amount of columns you will span
                    c5.setLayoutParams(params);

                    table.addView(tr2, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public List<TimeRegTask> getWeekList(String dateAsStr, final View rootView) {
        MySQLiteHelper db = new MySQLiteHelper(rootView.getContext());
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

    /*
    public void init(View rootView) {
        TableLayout stk = (TableLayout) rootView.findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(getContext());
        TextView tv0 = new TextView(getContext());
        tv0.setText(" Sl.No ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(getContext());
        tv1.setText(" Product ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(getContext());
        tv2.setText(" Unit Price ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(getContext());
        tv3.setText(" Stock Remaining ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        for (int i = 0; i < 25; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t1v = new TextView(getContext());
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(getContext());
            t2v.setText("Product " + i);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(getContext());
            t3v.setText("Rs." + i);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(getContext());
            t4v.setText("" + i * 15 / 32 * 10);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
        }

    }
    */

}
