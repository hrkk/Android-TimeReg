package timereg.roninit.dk.timereg;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timereg.roninit.dk.timereg.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class OverviewActivityFragment extends Fragment {

    private Spinner spinner2;
    private View rootView;

    private LinearLayout lm;
    public OverviewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_overview, container, false);
       // lm = (LinearLayout) rootView.findViewById(R.id.linearMain);
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
                Collection<List<TimeRegTask>> collection = getWeekCompanyLists(tasks);
                weekTotalHours.setText("" + Util.getDayTotalHours(tasks) + " timer");


                LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.ow_sw_linearlayout);

                TextView textView = (TextView) rootView.findViewById(R.id.ow_submitStatus);
                if (!tasks.isEmpty())
                    textView.setText(tasks.get(0).getSubmitDate() != null ? " Godkent " + tasks.get(0).getSubmitDate() : "Ikke godkendt");


                Iterator<List<TimeRegTask>> listIterator = collection.iterator();

                while (listIterator.hasNext()){
                    List<TimeRegTask> timeRegTasks = listIterator.next();
                    // lets find the name
                    // Create LinearLayout
                    LinearLayout ll = new LinearLayout(rootView.getContext());
                    ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    // Create TextView
                    TextView product = new TextView(rootView.getContext());
                    product.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    product.setText("Virksomhed: ");
                    ll.addView(product);

                    // Create TextView
                    TextView price = new TextView(rootView.getContext());
                    product.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    price.setText(" " + timeRegTasks.get(0).getCompany());
                    price.setTypeface(null, Typeface.BOLD);
                    ll.addView(price);

                    //Add button to LinearLayout defined in XML
                    linearLayout.addView(ll);

                    TableLayout table1 = new TableLayout(rootView.getContext());
                //    table1.setStretchAllColumns(true);
               //     table1.setShrinkAllColumns(true);
//                    table1.setColumnStretchable(0, true);
//                    table1.setColumnShrinkable(0, false); //
//                    table1.setColumnStretchable(1, false);
//                    table1.setColumnShrinkable(1, true); // laver tekst wrap
              //     table1.setColumnStretchable(2, true);
             //       table1.setColumnShrinkable(2,false); //
                    table1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    // header
                    TableRow tr = new TableRow(getContext());

                    TextView c1 = new TextView(getContext());
                    c1.setText("Dato");
                    c1.setBackgroundColor(Color.WHITE);

                    TextView c2 = new TextView(getContext());
                    c2.setText("Opg. nr. og navn");
                    c2.setBackgroundColor(Color.WHITE);
                    c2.setPadding(5,0,5,0);
                    c2.setMaxLines(10);
                    c2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));

                    TextView c3 = new TextView(getContext());
                    c3.setText("Timer");
                    c2.setPadding(0, 0, 20, 0);
                    c3.setGravity(Gravity.RIGHT);
                    c3.setBackgroundColor(Color.WHITE);

//                    TextView c4 = new TextView(getContext());
//                    c4.setText("Beskrivelse");
//                    c4.setBackgroundColor(Color.WHITE);

                    tr.addView(c1);
                    tr.addView(c2);
                    tr.addView(c3);
                //    tr.addView(c4);


                    table1.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                  //  BigDecimal bd = new BigDecimal("0.00");
                    for (TimeRegTask e : timeRegTasks) {

                         tr = new TableRow(getContext());

                         c1 = new TextView(getContext());
                        c1.setText(String.format("%s (%s)", e.getDate(), DateUtil.getOnlyDayOfWeek(e.getDate())));
                        c1.setBackgroundColor(Color.WHITE);

                         c2 = new TextView(getContext());
                        c2.setText(e.getTaskNumberAndName());
                        c2.setBackgroundColor(Color.WHITE);
                        c2.setPadding(5, 0, 5, 0);
                        c2.setMaxLines(10);
                        c2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
                      //  c2.setHeight(LayoutParams.WRAP_CONTENT);

                         c3 = new TextView(getContext());
                        c3.setText(String.format("%s", e.getHours()));
                        c3.setBackgroundColor(Color.WHITE);
                        c3.setGravity(Gravity.RIGHT);
                     //   c3.setTextAlignment(1);
//
//                         c4 = new TextView(getContext());
//                        c4.setText(e.getAdditionInfomation());
//                        c4.setBackgroundColor(Color.WHITE);

                        tr.addView(c1);
                        tr.addView(c2);
                        tr.addView(c3);
                     //   tr.addView(c4);

                    //    bd.add(new BigDecimal(e.getHours()));
                        table1.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                    }


                    linearLayout.addView(table1);

                    TableLayout table2 = new TableLayout(rootView.getContext());
                    table2.setStretchAllColumns(true);
                    table2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    TableRow hoursTotal = new TableRow(getContext());
                    TextView c12 = new TextView(getContext());
                    c12.setText("Timer i alt:");
                    c12.setBackgroundColor(Color.WHITE);
                    hoursTotal.addView(c12);
                    TextView c13 = new TextView(getContext());
                    c13.setText("" + Util.getDayTotalHours(timeRegTasks));
                    c13.setBackgroundColor(Color.WHITE);
                    c13.setGravity(Gravity.RIGHT);
                    hoursTotal.addView(c13);
                    table2.addView(hoursTotal, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                    // add empty row
                    tr = new TableRow(getContext());
                    c1 = new TextView(getContext());
                    c1.setText("Timer i alt inddelt efter opgave:");
                    tr.addView(c1);
                    table2.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                    Map<String, List<TimeRegTask>> map = Util.splitTimeRegs(timeRegTasks);

                    Set<String> keys = map.keySet();

                    for(String e: keys) {
                        TableRow tr3 = new TableRow(getContext());

                        TextView c15 = new TextView(getContext());
                        c15.setText(e);
                        c15.setBackgroundColor(Color.WHITE);
                        tr3.addView(c15);

                        TextView c14 = new TextView(getContext());
                        c14.setText("" + Util.getDayTotalHours(map.get(e)));
                        c14.setBackgroundColor(Color.WHITE);
                        c14.setGravity(Gravity.RIGHT);
                        //   c13.setTe
                        tr3.addView(c14);


                        table2.addView(tr3, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));


                    }


                    tr = new TableRow(getContext());
                    c1 = new TextView(getContext());
                    tr.addView(c1);
                    table2.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));



                    linearLayout.addView(table2);

//                    // new row for description
//                    TableRow hoursTotal = new TableRow(getContext());
//
//
//                    TextView c12 = new TextView(getContext());
//                    c12.setBackgroundColor(Color.WHITE);
//                    c12.setText("Timer i alt:");
//                    hoursTotal.addView(c12);
//                    TextView c13 = new TextView(getContext());
//                    c13.setBackgroundColor(Color.WHITE);
//                    c13.setText("notused");
//                   // c13.setGravity(Gravity.RIGHT);
//                   // c2.setPadding(5, 0, 5, 0);
//
//                   // c2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
//                    hoursTotal.addView(c13);
////                    TableRow.LayoutParams params = (TableRow.LayoutParams) c13.getLayoutParams();
////                    params.span = 1;
////                    params.rightMargin = 20;
////                    c13.setLayoutParams(params);
//
//
//                    TextView c5 = new TextView(getContext());
//                    c5.setText(""+Util.getDayTotalHours(timeRegTasks));
//                    c5.setBackgroundColor(Color.WHITE);
//                     c5.setGravity(Gravity.RIGHT);
//                    hoursTotal.addView(c5);
////                    TableRow.LayoutParams params = (TableRow.LayoutParams) c5.getLayoutParams();
////                //    params.span = 3; //amount of columns you will span
////                    c5.setLayoutParams(params);
//
//                    table1.addView(hoursTotal, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));






                }




                //
                /*
                for (int j = 0; j <= 4; j++) {
                    // Create LinearLayout
                    LinearLayout ll = new LinearLayout(rootView.getContext());
                    ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    // Create TextView
                    TextView product = new TextView(rootView.getContext());
                    product.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    product.setText("Virksomhed" + j + "    ");
                    ll.addView(product);

                    // Create TextView
                    TextView price = new TextView(rootView.getContext());
                    product.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    price.setText("  $" + j + " BEC");
                    ll.addView(price);

                    //Add button to LinearLayout defined in XML
                    linearLayout.addView(ll);
                }
                */





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

                /*
                TableLayout table = (TableLayout) rootView.findViewById(R.id.lastExpensesTable);

                // lets clear all first
                table.removeViews(1, table.getChildCount() - 1);
                table.setStretchAllColumns(true);


                for (TimeRegTask e : tasks) {

                    TableRow tr = new TableRow(getContext());

                    TextView c1 = new TextView(getContext());
                    c1.setText(DateUtil.getFormattedDateWithWeekDay(e.getDate()));
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
                */
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Collection<List<TimeRegTask>> getWeekCompanyLists(List<TimeRegTask> weekList){

        //
        Map<String, List<TimeRegTask>> map = new HashMap<String,  List<TimeRegTask>>();

        for(TimeRegTask e : weekList) {

            if (map.containsKey(e.getCompany())) {
                // add to list
                List<TimeRegTask> timeRegTasks = map.get(e.getCompany());
                timeRegTasks.add(e);
            } else {
                List<TimeRegTask> timeRegTasks = new ArrayList<TimeRegTask>();
                timeRegTasks.add(e);
                map.put(e.getCompany(), timeRegTasks);
            }
        }

        Collection<List<TimeRegTask>> values = map.values();

        return values;
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
