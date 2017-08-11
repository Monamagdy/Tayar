package com.example.monamagdy.tayar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseQuery;
import com.parse.ParseObject;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by monamagdy on 2/28/16.
 */
public class admin_view extends Activity {

    String db_tayar;
    ArrayAdapter<String> adapter;
    ListView k;
    ArrayList<String> listItems0;
    Date today;
    Date tomorrow;
    String itemValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view);
        k = (ListView) findViewById(R.id.listView2);
        final ArrayList<String> myList = (ArrayList<String>) getIntent().getStringArrayListExtra("list");

        final int intValue = getIntent().getIntExtra("position", 0);

        Log.d("jj", myList.toString());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        k.setAdapter(adapter);
               k.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Egypt"));
                String noti = (String) k.getItemAtPosition(position);
                Calendar rightNow = Calendar.getInstance();
                Intent intent= new Intent(admin_view.this,dial.class);
                cal.set(Calendar.HOUR_OF_DAY, 6);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);

                // start of today
                int hour_of_day_now=rightNow.get(Calendar.HOUR_OF_DAY);
                int six_am=cal.get(Calendar.HOUR_OF_DAY);
                // start of tomorrow

                int diff=hour_of_day_now-six_am;
                if(diff<0)
                {
                    cal.add(Calendar.DAY_OF_MONTH,-1);
                    today = cal.getTime();               //6am
                    tomorrow = rightNow.getTime();
                }
                else
                {
                    today = cal.getTime(); //6am
                    tomorrow = rightNow.getTime();

                }
                if(intValue==0) //fresh orders
                {


                    noti=noti.substring(noti.indexOf(':')+1,noti.indexOf('\n'));
                    Log.d("position",noti);
                    intent.putExtra("pn",noti);
                 startActivity(intent);

                }

              else  if (intValue == 3) {
                    myList.size();
                    // ListView Clicked item index
                    int itemPosition = position;
                    String listPosition;      listPosition = String.valueOf(parent.getItemAtPosition(position));
                    // ListView Clicked item value
                itemValue = (String) k.getItemAtPosition(position);

                    // if (listPosition == itemValue) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Not").orderByDescending("createdAt");
                    query.whereGreaterThanOrEqualTo("createdAt", today); //6am
                    query.whereLessThan("createdAt", tomorrow); //12pm

                    query.whereEqualTo("Order_Status", "Served");
                    //     query.whereEqualTo("Handled_by", itemValue);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            String time = "", mm = "", s = "";
                            int above = 0, under = 0, minutes = 0, k;
                            //    listItems3.add("Today's served orders are "+objects.size());
                            if (e == null) {
                                listItems0 = new ArrayList<String>();
                                int orders = 0;
                                for (int i = 0; i < objects.size(); i++) {

                                    ParseObject ob = objects.get(i);

                                    s = ob.get("Handled_by").toString();
                                    if (s.equals(itemValue)) {

                                        orders++;
                                        if (ob.get("Time_to_serve_order") != null)
                                            db_tayar = ob.get("Time_to_serve_order").toString();

                                        k = db_tayar.indexOf(":");
                                      if (k==2) //two digits are used to represent the hours--> greater than 50
                                      {
                                          above++;
                                          continue;
                                      }
                                        else if(k==1) //one digit is used to represent hours
                                       if(db_tayar.charAt(0)!='0') //get the first char, if !=0 -> an hour has passed
                                       {
                                           above++;
                                           continue;
                                            }

                                if(db_tayar.charAt(k+1)=='0') {
                                    under++;
                                    continue;
                                }
                                    else {
                                    under++;
                                    continue;
                                }

                                    }
                                }
                                if (!listItems0.contains(itemValue + " served " + orders + " orders today." + '\n' + under + ": اقل من خمسين دقيقة " + '\n' + above + ": اكثر من خمسين دقيقة"))
                                    listItems0.add(itemValue + " served " + orders + " orders today." + '\n' + under + ": اقل من خمسين دقيقة" + '\n' + above + ": اكثر من خمسين دقيقة");

                                Intent intent = new Intent(admin_view.this, admin_tayareen_summary.class);
                                intent.putStringArrayListExtra("list", listItems0);
                                Log.d("New3", listItems0.toString());
                                startActivity(intent);
                            } else {

                            }

                        }
                    });
                } else if (intValue == 4) {
                    ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
                    itemValue = (String) k.getItemAtPosition(position);

                    query.whereEqualTo("username", itemValue);
                   query.getFirstInBackground(new GetCallback<ParseUser>() {
                       public void done(final ParseUser user, ParseException e) {
                           if (e == null) {
                               AlertDialog.Builder alert_builder = new AlertDialog.Builder(admin_view.this);
                               // String status = object.get("Order_Status").toString();
                               String state = user.get("Tayar_State").toString();
                               if (state.contains("serving")) {
                                   alert_builder.setMessage("This Tayar is currently serving");
                                   AlertDialog alert = alert_builder.create();
                                   alert.show();
                               } else {
                                   alert_builder.setMessage("This Tayar is free");
                                   AlertDialog alert = alert_builder.create();
                                   alert.show();
                               }
                           }
                           else {

                           }

                       }
                   });

                }


            }

        });
    }
}