package com.example.monamagdy.tayar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.parse.ParseCloud;
import android.content.SharedPreferences;
import java.util.HashMap;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.FunctionCallback;
import java.text.SimpleDateFormat;
import com.parse.ParseACL;
import java.util.Date;
import android.app.ProgressDialog;

/**
 * Created by monamagdy on 3/2/16.
 */
public class done extends Activity {


    boolean finish=false;
    SharedPreferences prefs;
    String MY_PREFS_NAME="MyPrefsFile";
    String c;
    TextView message;
    Button stopButton;
    ProgressDialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done);
       message= (TextView)findViewById(R.id.textView14);
        stopButton = (Button) findViewById(R.id.button12);
        Button call= (Button)findViewById(R.id.button10);

        if(!finish) {

                            dialog1 = new ProgressDialog(done.this); // this = YourActivity
                            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            dialog1.setMessage("تحميل...");
                            dialog1.setIndeterminate(false);
                            dialog1.setCanceledOnTouchOutside(false);
                            dialog1.setCancelable(true);
                            dialog1.show();

            execute();
        }

        call.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent= new Intent(done.this,dial.class);
                String not = getIntent().getExtras().getString("phone");
                intent.putExtra("pn",not);
                startActivity(intent);
            }

        });


                stopButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        AlertDialog.Builder alert_builder = new AlertDialog.Builder(done.this);
                        alert_builder.setMessage("هل متاكد من انهاء هذا الطلب").setCancelable(false)
                                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish = true;

                                        c = ParseUser.getCurrentUser().getObjectId().toString();
                                        String name=getIntent().getExtras().getString("name");
                                        String branch = getIntent().getExtras().getString("branch");
                                        String phone= getIntent().getExtras().getString("phone");
                                        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Not");
                                        Log.d("name",name);
                                        Log.d("branch",branch);
                                        Log.d("phone",phone);
                                        query.whereEqualTo("Client_Number", phone);
                                        query.whereEqualTo("Client_Name",name);
                                        query.whereEqualTo("Branch_Name", branch);
                                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                            public void done(final ParseObject object, ParseException e) {

                                                if (object == null) {
                                                    Log.d("Donewithorder", "This notification is saved as null.");
                                                } else {
                                                    ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {
                                                        public void done(String result, ParseException e) {
                                                            String take_time;
                                                            String time_elapsed;
                                                            if (e == null) {
                                                                Log.d("Cloud Code", result);
                                                                object.put("Order_Status", "Served");
                                                                ParseUser.getCurrentUser().increment("Orders");
                                                                ParseUser.getCurrentUser().put("Tayar_State", "free");
                                                                ParseUser.getCurrentUser().saveInBackground();
                                                                object.put("Time_to_serve_order", result);
                                                                ParseACL acl = new ParseACL();
                                                                acl.setPublicReadAccess(true);
                                                                object.setACL(acl);
                                                                try {
                                                                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z");
                                                                    Date done = formatter.parse(result);
                                                                    Long time_done = done.getTime(); //UTC Time
                                                                    // time_now=7200000+time_now; //add two hours to get GMT
                                                                    take_time = object.get("Time_Take_was_pressed").toString();
                                                                    Date take = formatter.parse(take_time);
                                                                    Long time_take = take.getTime();
                                                                    printDifference(done, take);

                                                                    time_elapsed = showElapsedTime(time_done, time_take);
                                                                    object.put("Time_to_serve_order", time_elapsed);
                                                                    //  Log.d("Long time NOW", String.valueOf(time_now));
                                                                    // Log.d("time take was pressed", String.valueOf(time_take));
                                                                    // message.setText("Time elapsed till the moment" + time_elapsed);
                                                                //    stopButton.setVisibility(View.GONE);
                                                                    Intent x = new Intent(done.this, Tayar_Landing_page.class);
                                                                  startActivity(x);
                                                                    Toast.makeText(done.this, "It took you " + time_elapsed + " to serve this order", Toast.LENGTH_LONG).show();
                                                                } catch (java.text.ParseException ee) {
                                                                    // TODO Auto-generated catch block
                                                                    ee.printStackTrace();
                                                                }

                                                                object.saveInBackground(new SaveCallback() {
                                                                    public void done(ParseException e) {
                                                                        if (e != null) {
                                                                            e.printStackTrace();
                                                                            Log.d("time done is pressed", String.valueOf(object.getUpdatedAt().getTime()));
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    });
                                                }

                                            }
                                        });


                                    }
                                })
                                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alert_builder.create();
                        alert.show();


                    }

                });


            }

            private String showElapsedTime(long big, long small) {

                prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                // long time_take_button = prefs.getString(c, null);
                long timeElapsed = big - small;
                int seconds = (int) (timeElapsed / 1000) % 60;
                int min = (int) ((timeElapsed / (1000 * 60)) % 60);
                int hour = (int) ((timeElapsed / (1000 * 60 * 60)) % 24);
                String msg = hour + ":" + min + ":" + seconds;
                message.setText(msg);
                //last = false;

                Log.d("Time Elapsed", msg);
                return msg;
            }

            private void execute() {


                c = ParseUser.getCurrentUser().getObjectId().toString();
                String name=getIntent().getExtras().getString("name");
                String branch = getIntent().getExtras().getString("branch");
                String phone= getIntent().getExtras().getString("phone");
                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Not");
                Log.d("name",name);
                Log.d("branch",branch);
                Log.d("phone",phone);
                query.whereEqualTo("Client_Number", phone);
                query.whereEqualTo("Client_Name", name);
                query.whereEqualTo("Branch_Name", branch);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {

                        if (object == null) {
                            Log.d("execute", "This notification is saved as null.");
                        } else {
                            ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {
                                public void done(String result, ParseException e) {
                                    String take_time;
                                    Date createed;
                                    String time_elapsed;
                                    if (e == null) {
                                        Log.d("Cloud Code", result);
                                        try {
                                            take_time = object.get("Time_Take_was_pressed").toString();
                                            createed= object.getCreatedAt();
                                            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z");
                                            Date now = formatter.parse(result);
                                            Long time_now = now.getTime(); //UTC Time
                                            // time_now=7200000+time_now; //add two hours to get GMT
                                            Date take = formatter.parse(take_time);
                                            Long time_take = take.getTime();
                                            Log.d("TIME FOR 2 MIN","ss");
                                            printDifference(createed,now);
                                            //Date j= now-createed;
                                            // time_take=7200000+time_take;

                                            printDifference(now, take);
                                            time_elapsed = showElapsedTime(time_now, time_take);
                                            object.put("Time_till_the_moment", time_elapsed);
                                            Log.d("Long time NOW", String.valueOf(time_now));
                                            Log.d("time take was pressed", String.valueOf(time_take));
                                            message.setText("الوقت المستهلك حتي الان " + time_elapsed);
                                            //  Toast.makeText(done.this, "It took you " + time_elapsed + " to serve this order", Toast.LENGTH_LONG).show();
                                            dialog1.dismiss();
                                        } catch (java.text.ParseException ee) {
                                            // TODO Auto-generated catch block
                                            ee.printStackTrace();
                                        }
                                    }
                                }
                            });

                        }

                    }
                });


            }

            public void printDifference(Date startDate, Date endDate) {

                //milliseconds
                long different = endDate.getTime() - startDate.getTime();

                System.out.println("startDate : " + startDate);
                System.out.println("endDate : " + endDate);
                System.out.println("different : " + different);

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;

                long elapsedSeconds = different / secondsInMilli;

                System.out.printf(
                        "%d days, %d hours, %d minutes, %d seconds%n",
                        elapsedDays,
                        elapsedHours, elapsedMinutes, elapsedSeconds);

            }
        }