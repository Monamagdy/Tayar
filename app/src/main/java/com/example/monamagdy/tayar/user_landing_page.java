package com.example.monamagdy.tayar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by monamagdy on 2/28/16.
 */
public class user_landing_page extends Activity {
    public static long time;
    ArrayList<String> listItems0 ;
    ProgressDialog dialog1;
    String db_notification;
    ArrayAdapter<String> adapter;
    int pos;
   public static boolean sign=false;
    Date today, tomorrow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_landing_page);
        ListView k = (ListView) findViewById(R.id.listView00);
        Button signout=(Button)findViewById(R.id.button7);
        String [] options= {"أطلب طيار","طلبات في السكه","الطلبات السابقه"};
        final String MY_PREFS_NAME="MyPrefsFile";
        final SharedPreferences.Editor editor=getBaseContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        k.setAdapter(adapter);
        k.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {
                        dialog1 = new ProgressDialog(user_landing_page.this); // this = YourActivity
                        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog1.setMessage("تحميل...");
                        dialog1.setIndeterminate(false);
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.setCancelable(true);
                        dialog1.show();

                        //    String itemValue = (String) k.getItemAtPosition(position);
                        //     Calendar cal = new GregorianCalendar();
                        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Egypt"));

                        Calendar rightNow = Calendar.getInstance();

                        cal.set(Calendar.HOUR_OF_DAY, 6);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        //    cal.add(Calendar.HOUR_OF_DAY, -6);

                        // start of today
                        int hour_of_day_now = rightNow.get(Calendar.HOUR_OF_DAY);
                        int six_am = cal.get(Calendar.HOUR_OF_DAY);
                        // start of tomorrow

                        int diff = hour_of_day_now - six_am;
                        if (diff < 0) {
                            cal.add(Calendar.DAY_OF_MONTH, -1);
                            today = cal.getTime();               //6am
                            tomorrow = rightNow.getTime();
                        } else {
                            today = cal.getTime(); //6am
                            tomorrow = rightNow.getTime();

                        }
                        if (position == 0) {
                            Intent intent = new Intent(user_landing_page.this, notify.class);
                            startActivity(intent);
                            dialog1.dismiss();
                        }
                        if (position==1)
                        {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Not").orderByDescending("createdAt");
                            query.whereGreaterThanOrEqualTo("createdAt", today);
                            query.whereLessThan("createdAt", tomorrow);

                            query.whereContains("Client_Name", ParseUser.getCurrentUser().getUsername().toString());
                            query.whereNotEqualTo("Order_Status", "Served"); //pending and being served
                            listItems0 = new ArrayList<String>();
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> objects, ParseException e) {
                                    String time_of_notification = "";
                                    String client_phone_number = "";
                                    if (e == null) {
                                        for (int i = 0; i < objects.size(); i++) {
                                            ParseObject ob = objects.get(i);
                                            if (ob.get("Client_Number") != null) {
                                                client_phone_number = ob.get("Client_Number").toString();
                                                if (ob.getCreatedAt() != null)
                                                    time_of_notification = ob.getCreatedAt().toString();
                                                time_of_notification = time_of_notification.substring(0, time_of_notification.lastIndexOf(":"));
                                                time_of_notification = time_of_notification.substring(0, time_of_notification.lastIndexOf(":"));

                                                db_notification = "طلبك إلي عميل #" + " "+client_phone_number+" "+"في السكه";

                                                Log.d("Notification Retrieved", db_notification);
                                                if (!listItems0.contains(db_notification + '\n' + "Date: " + time_of_notification))
                                                    listItems0.add(db_notification + '\n' + "Date: " + time_of_notification);

                                            } else {

                                            }
                                        }
                                    } else

                                    {

                                    }

                                    Intent intent = new Intent(user_landing_page.this, user_history.class);
                                    intent.putStringArrayListExtra("list", listItems0);
                                    startActivity(intent);
                                    dialog1.dismiss();
                                }
                            });


                        }


                        else if (position == 2)

                        {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Not").orderByDescending("createdAt");
                            query.whereGreaterThanOrEqualTo("createdAt", today);
                            query.whereLessThan("createdAt", tomorrow);

                            query.whereContains("Client_Name", ParseUser.getCurrentUser().getUsername().toString());
                            query.whereEqualTo("Order_Status", "Served");
                            listItems0 = new ArrayList<String>();
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> objects, ParseException e) {
                                    String time_of_notification = "";
                                    String client_phone_number = "";
                                    if (e == null) {
                                        for (int i = 0; i < objects.size(); i++) {
                                            ParseObject ob = objects.get(i);
                                            if (ob.get("Client_Number") != null) {
                                                client_phone_number = ob.get("Client_Number").toString();
                                                if (ob.getCreatedAt() != null)
                                                    time_of_notification = ob.getCreatedAt().toString();
                                                time_of_notification = time_of_notification.substring(0, time_of_notification.lastIndexOf(":"));
                                                time_of_notification = time_of_notification.substring(0, time_of_notification.lastIndexOf(":"));

                                                db_notification = "طلبت طيار الى عميل # " + client_phone_number;

                                                Log.d("Notification Retrieved", db_notification);
                                                if (!listItems0.contains(db_notification + '\n' + "Date: " + time_of_notification))
                                                    listItems0.add(db_notification + '\n' + "Date: " + time_of_notification);

                                            } else {

                                            }
                                        }
                                    } else

                                    {

                                    }

                                    Intent intent = new Intent(user_landing_page.this, user_history.class);
                                    intent.putStringArrayListExtra("list", listItems0);
                                    startActivity(intent);
                                    dialog1.dismiss();
                                }
                            });


                        }
                    }
                });
        signout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog1 = new ProgressDialog(user_landing_page.this); // this = YourActivity
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

                Intent intent = new Intent(user_landing_page.this, login.class);
                startActivity(intent);
                sign = true;
            }

        });
    }

    @Override
    public void onBackPressed() {

    }

}


