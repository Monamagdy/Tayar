package com.example.monamagdy.tayar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.content.Intent;

import com.parse.FunctionCallback;
import com.parse.LogOutCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;;
import com.parse.FindCallback;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import android.widget.Button;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by monamagdy on 2/28/16.
 */
public class entry extends Activity{
    ListView lv;
    Intent intent;
    ProgressDialog dialog1;
    String db_notification,db_tayar;
    ArrayAdapter<String> adapter;
    public static boolean sign=false;
    ArrayList<String> listItems0  = new ArrayList<String>();
    ArrayList<String> listItems1 = new ArrayList<String>();
    ArrayList<String> listItems2= new ArrayList<String>();
    ArrayList<String> listItems3= new ArrayList<String>();
    ArrayList<String> listItems4= new ArrayList<String>();
    ArrayList<String> listItems5= new ArrayList<String>();
    Date today,tomorrow;
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.entry);
            lv = (ListView) findViewById(R.id.listView77);
            Button signout = (Button) findViewById(R.id.button2);

            String[] values = new String[]
                    {"طلبات جديدة", "طلبات في الخدمة", "طلبات اليوم المنتهية","عدد الطلبات لكل طيار","اشغاليه الطيارين","الإشعارات"};

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
            final String MY_PREFS_NAME="MyPrefsFile";
            final SharedPreferences.Editor editor=getBaseContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

            // Assign adapter to ListView
            lv.setAdapter(adapter);

            // ListView Item Click Listener
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                     dialog1 = new ProgressDialog(entry.this); // this = YourActivity
                    dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog1.setMessage("تحميل...");
                    dialog1.setIndeterminate(false);
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.setCancelable(true);
                    dialog1.show();

                    // ListView Clicked item index
                    int itemPosition = position;
                    final String listPosition = String.valueOf(parent.getItemAtPosition(position));
                    // ListView Clicked item value
                    String itemValue = (String) lv.getItemAtPosition(position);

                    Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Egypt"));

                    Calendar rightNow = Calendar.getInstance();

                    cal.set(Calendar.HOUR_OF_DAY, 6);
                    cal.set(Calendar.MINUTE,0);
                    cal.set(Calendar.SECOND,0);
                    cal.set(Calendar.MILLISECOND,0);
                    //    cal.add(Calendar.HOUR_OF_DAY, -6);

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

                    if (position == 0) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Not").orderByDescending("createdAt");
                        // query.whereContains("createdAt",cal.getTime().toString());
                        query.whereGreaterThanOrEqualTo("createdAt", today);
                        query.whereLessThanOrEqualTo("createdAt", tomorrow);
                      //  query.whereLessThanOrEqualTo("createdAt",after_tom);
                        query.whereEqualTo("Order_Status", "pending");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                String time="";
                                String pn=";";
                                String name="";
                                String branch="";
                                if (e == null) {
                                    for (int i = 0; i < objects.size(); i++) {
                                        ParseObject ob = objects.get(i);

                                        if(ob.get("Client_Number")!=null)
                                        pn= ob.get("Client_Number").toString();

                                        if(ob.get("Client_Name")!=null)
                                        name=ob.get("Client_Name").toString();
                                        if(ob.get("Branch_Name")!=null)
                                        branch=ob.get("Branch_Name").toString();

                                        db_notification =" عميل "+name+ "  فرع "+branch+" طلب طيار برقم:"+pn;
                                        if(ob.getCreatedAt()!=null) {
                                            time = ob.getCreatedAt().toString();
                                            time=time.substring(0,time.lastIndexOf(":")) ;
                                            time=time.substring(0,time.lastIndexOf(":"));

                                        }

                                        if (!listItems0.contains(db_notification+'\n'+"Date: "+time))
                                            listItems0.add(db_notification+'\n'+"Date: "+time);
                                    }
                                } else {

                                }
                                Intent intent = new Intent(entry.this, admin_view.class);
                                intent.putStringArrayListExtra("list", listItems0);
                                intent.putExtra("position", position);
                                Log.d("New", listItems0.toString());
                                startActivity(intent);
                                dialog1.dismiss();
                            }
                        });


                    } else if (position == 1) {


                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Not").orderByDescending("createdAt");
                        query.whereGreaterThanOrEqualTo("createdAt", today);
                        query.whereLessThanOrEqualTo("createdAt", tomorrow);
                        query.whereEqualTo("Order_Status", "Serving");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(final List<ParseObject> objects, ParseException e) {

                        Log.d("database query", "started");
                                if (e == null) {

                                    Log.d("database query","ok");
                                    ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {

                                        public void done(String result, ParseException e) {
                                          String s;
                                            String pn="";
                                            String name="";
                                            String branch="";
                                          String time = "";
                                          String elapsed = "";
                                          String take_time="";
                                            if (e == null) {
                                                Log.d("Cloud Code", result);
                                               if(!listItems1.isEmpty())
                                                listItems1.clear();
                                                for (int i = 0; i < objects.size(); i++) {
                                                    ParseObject ob = objects.get(i);

                                                    if(ob.get("Client_Number")!=null)
                                                        pn= ob.get("Client_Number").toString();

                                                    if(ob.get("Client_Name")!=null)
                                                        name=ob.get("Client_Name").toString();
                                                    if(ob.get("Branch_Name")!=null)
                                                        branch=ob.get("Branch_Name").toString();

                                                    s = "عميل"+name+ " فرع"+branch+" طلب طيار برقم:"+pn;
                                                   Log.d("s", s);
                                                    if (ob.getCreatedAt() != null)
                                                        time = ob.getCreatedAt().toString();
                                                    if (ob.get("Handled_by") != null)
                                                        db_tayar = "Handled by: " + ob.get("Handled_by").toString();
                                                    if (ob.get("Time_Take_was_pressed") != null)
                                                    {
                                                        try {
                                                            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z");
                                                            Date now = formatter.parse(result);
                                                            Long time_now = now.getTime(); //UTC Time
                                                            // time_now=7200000+time_now; //add two hours to get GMT
                                                            take_time = ob.get("Time_Take_was_pressed").toString();
                                                            Date take = formatter.parse(take_time);
                                                            Long time_take = take.getTime();
                                                            elapsed = showElapsedTime(time_now, time_take);
                                                            //  Log.d("Long time NOW", String.valueOf(time_now));
                                                            // Log.d("time take was pressed", String.valueOf(time_take));
                                                        } catch (java.text.ParseException ee) {
                                                            // TODO Auto-generated catch block
                                                            ee.printStackTrace();
                                                        }


                                                    }

                                                   // if (!listItems1.contains(s + '\n' + time + "." + '\n' + db_tayar + '\n' + "لوقت المستهلك حتي الان"))
                                                    listItems1.add(s + '\n' + time + "." + '\n' + db_tayar + '\n' + "الوقت المستهلك حتي الان " + elapsed);



                                                }

                                            }

                                            Intent intent = new Intent(entry.this, admin_view.class);
                                            intent.putStringArrayListExtra("list",listItems1);
                                            intent.putExtra("position", position);
                                            Log.d("New1", listItems1.toString());
                                            startActivity(intent);
                                            dialog1.dismiss();
                                        }
                                    });
                                        }

                                        else

                                        {

                                        }

                                    }
                                });

                            }

                            else if(position==2)

                            {

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Not").orderByDescending("createdAt");
                                query.whereGreaterThanOrEqualTo("createdAt", today);
                                query.whereLessThanOrEqualTo("createdAt", tomorrow);
                                query.whereEqualTo("Order_Status", "Served");
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        String s, time = "", timee = "",branch="",name="",pn="";
                                        if (e == null) {
                                            for (int i = 0; i < objects.size(); i++) {
                                                ParseObject ob = objects.get(i);
                                                if (ob.get("Client_Number") != null) {
                                                    pn= ob.get("Client_Number").toString();

                                                    if(ob.get("Client_Name")!=null)
                                                        name=ob.get("Client_Name").toString();
                                                    if(ob.get("Branch_Name")!=null)
                                                        branch=ob.get("Branch_Name").toString();

                                                    s = "عميل"+" "+name+" " +" فرع"+" "+branch+" طلب طيار برقم:"+pn;
                                                    Log.d("s", s);
                                                    if(ob.getCreatedAt()!=null) {
                                                        timee = ob.getCreatedAt().toString();
                                                        timee=timee.substring(0,timee.lastIndexOf(":")) ;
                                                        timee=timee.substring(0,timee.lastIndexOf(":"));

                                                    }

                                                    //Log.d("s", s);
                                                    if (ob.get("Handled_by") != null)
                                                        db_tayar = "Handled by: " + ob.get("Handled_by").toString();
                                                    if (ob.get("Time_to_serve_order") != null)
                                                        time = ob.get("Time_to_serve_order").toString();
                                                    if (!listItems2.contains(s + '\n' + "Date: "+timee + "." + '\n' + db_tayar + '\n' + "It took " + time + " to serve the order"))
                                                        listItems2.add(s + '\n' +"Date: "+ timee + "." + '\n' + db_tayar + '\n' + "It took " + time + " to serve the order");
                                                }
                                            }
                                        }
                                        else { //belongs to   if (e == null)

                                        }
                                        Intent intent = new Intent(entry.this, admin_view.class);
                                        intent.putStringArrayListExtra("list", listItems2);
                                        Log.d("New2", listItems2.toString());
                                        startActivity(intent);
                                        dialog1.dismiss();
                                    }
                                });

                            }

                            else if(position==3) //view all tayars

                            {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Not");
                                query.whereGreaterThanOrEqualTo("createdAt", today);
                                query.whereLessThanOrEqualTo("createdAt", tomorrow);

                                query.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        String s;
                                        if (e == null) {
                                            for (int i = 0; i < objects.size(); i++) {
                                                ParseObject ob = objects.get(i);
                                                if (ob.get("Handled_by") != null) {
                                                    s = ob.get("Handled_by").toString();
                                                    Log.d("s", s);
                                                    if (!listItems3.contains(s))
                                                        listItems3.add(s);
                                                }
                                            }
                                        } else {

                                        }
                                        Intent intent = new Intent(entry.this, admin_view.class);
                                        intent.putStringArrayListExtra("list", listItems3);
                                        intent.putExtra("position", position);
                                        Log.d("New2", listItems3.toString());
                                        startActivity(intent);
                                        dialog1.dismiss();
                                    }
                                });


                            }

                            else    if(position==4)//free tayar

                            {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                query.whereEqualTo("Value", "TAYAR");

                                query.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        String s;
                                        if (e == null) {
                                            for (int i = 0; i < objects.size(); i++) {

                                                ParseObject ob = objects.get(i);
                                                if (ob.get("username") != null) {
                                                    s = ob.get("username").toString();
                                                    String state = ob.get("Tayar_State").toString();
                                                    Log.d("s", s);
                                                    if (!listItems4.contains(s+" is "+state))
                                                        listItems4.add(s+" is "+state);
                                                }
                                            }
                                        } else {

                                        }
                                        Intent intent = new Intent(entry.this, admin_view.class);
                                        intent.putStringArrayListExtra("list", listItems4);
                                        intent.putExtra("position", position);
                                        Log.d("New2", listItems4.toString());
                                        startActivity(intent);
                                        dialog1.dismiss();
                                    }
                                });


                            }
                    else if(position==5)
                    {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notifications").orderByDescending("createdAt");
                        query.whereGreaterThanOrEqualTo("createdAt", today); //6am
                        query.whereLessThan("createdAt", tomorrow); //12pm
                        listItems3=   new ArrayList<String>();
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {

                                if (e == null) {

                                    String orders = null;
                                    for (int i = 0; i < objects.size(); i++) {
                                        ParseObject ob = objects.get(i);
                                        if(ob.get("notifications")!=null) {
                                            orders = ob.get("notifications").toString();

                                            if (!(listItems5.contains(orders)))
                                                listItems5.add(orders);
                                        }
                                    }

                                }

                                Intent intent = new Intent(entry.this, display_notification.class);
                                intent.putStringArrayListExtra("you", listItems5);
                                for (int i = 0; i < listItems5.size(); i++) {
                                    Log.d("list", listItems5.get(i).toString());
                                }
                                startActivity(intent);
                                dialog1.dismiss();
                            }
                        });

                    }


                        }

                    });
            signout.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    dialog1 = new ProgressDialog(entry.this); // this = YourActivity
                    dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog1.setMessage("In Progress..");
                    dialog1.setIndeterminate(false);
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.setCancelable(true);
                    dialog1.show();

                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        ParseUser.logOutInBackground(new LogOutCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d("logout", "success");
                                    editor.remove("username");
                                    editor.remove("password");
                                    editor.remove("remember");

                                    editor.commit();
                                } else {
                                    Log.d("logout", "failed");
                                }
                            }
                        });
                    }

                    Intent intent = new Intent(entry.this, login.class);
                    startActivity(intent);
                    sign = true;
                }

            });
        }
    private String showElapsedTime(long big, long small) {
        // long time_take_button = prefs.getString(c, null);
        long timeElapsed = big - small;
        int seconds = (int) (timeElapsed / 1000) % 60;
        int min = (int) ((timeElapsed / (1000 * 60)) % 60);
        int hour = (int) ((timeElapsed / (1000 * 60 * 60)) % 24);
        String msg =  hour + ":" + min + ":" + seconds;

        Log.d("Time Elapsed", msg);
        return msg;
    }
    @Override
    public void onBackPressed() {}
        }
