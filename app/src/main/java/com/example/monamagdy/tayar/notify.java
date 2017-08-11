package com.example.monamagdy.tayar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.parse.ParseException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.parse.SendCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import com.parse.ParsePush;
import android.content.Intent;
import android.widget.Toast;
import android.app.ProgressDialog;
public class notify extends Activity {

      String title;
boolean ordered=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);
        String temp, temp1;

        Button tayra = (Button) findViewById(R.id.button8);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[2];


        temp = ParseUser.getCurrentUser().get("Branch1").toString();
        temp1 = ParseUser.getCurrentUser().get("Branch2").toString();

        if (!temp.isEmpty())
            items[0] = temp;
        else
            items[0] = "";

        if (!temp1.isEmpty())
            items[1] = temp1;
        else
            items[1] = "";

        final AlertDialog.Builder builder = new AlertDialog.Builder(notify.this);
        final AlertDialog.Builder builder3 = new AlertDialog.Builder(notify.this);
        builder3.setMessage("هذا الرقم غير صحيح");

        final EditText phone = (EditText) findViewById(R.id.editText12);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        final AlertDialog alert33=builder3.create();
        if(ordered)
            Log.d("aa", "rr");

        tayra.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final String text = spinner.getSelectedItem().toString();
                final String client_phone = phone.getText().toString();


                if (client_phone.isEmpty()) {
                    builder.setMessage("من فضلك أدخل رقم العميل");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                if(client_phone.length()>11)
                {
                    builder.setMessage("الرقم الذي ادخلته غير صحيح");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            else if(client_phone.contains(" "))
                        alert33.show();

                else {
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(notify.this);
                    alert_builder.setMessage("الرجاء تاكيد الطلب").setCancelable(false)
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    ProgressDialog dialog1 = new ProgressDialog(notify.this); // this = YourActivity
                                    dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    dialog1.setMessage("جاري ارسال الطلب");
                                    dialog1.setIndeterminate(true);
                                    dialog1.setCanceledOnTouchOutside(false);
                                    dialog1.show();
                                    if(isNetworkAvailable()==false) {
                                        dialog1.dismiss();
                                        alert33.setMessage("Make sure your device is internet connected!");
                                        alert33.show();

                                    }
                                        ParsePush push = new ParsePush();
                                    title = "من فضلك اذهب الي " + ParseUser.getCurrentUser().getUsername() + " " + "فرع" + " " + text + '\n' + " رقم العميل" + " " + client_phone;
                               //  final   String  mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                    push.setMessage(title);

                                    push.sendInBackground(new SendCallback() {
                                        public void done(ParseException e) {

                                            if (e == null) {

                                                ordered=true;
                                             final ParseObject gameScore = new ParseObject("Not");
                                                final ParseObject save_notification = new ParseObject("Notifications");
                                             //   String info= title + '\n' + " at " + mydate;
                                                String info= title;
                                              //  gameScore.put("ma", info);
                                                gameScore.put("Client_Name",ParseUser.getCurrentUser().getUsername());
                                                gameScore.put("Branch_Name",text);
                                                gameScore.put("Client_Number", client_phone);
                                                Log.d("aa", info);
                                                gameScore.put("Order_Status", "pending");
                                                gameScore.put("Alert_2min","false");
                                                save_notification.put("notifications", title);
                                                Log.d("notify", info);
                                                ParseACL acl = new ParseACL();
                                                acl.setPublicReadAccess(true);
                                                acl.setPublicWriteAccess(true);
                                                gameScore.setACL(acl);    /*updated here*/
                                                gameScore.saveInBackground();
                                                save_notification.setACL(acl);
                                                save_notification.saveInBackground();
                                                ParseUser.getCurrentUser().increment("Orders");
                                                ParseUser.getCurrentUser().saveInBackground();
                                                Log.d("push", "success!");
                                                Toast.makeText(getApplicationContext(), "الطيار فالطريق!"+'\n'+"شكراً لثقتكم في طيار!", Toast.LENGTH_LONG).show();
                                                   Intent intent= new Intent(notify.this, user_landing_page.class);
                                                startActivity(intent);

                                            } else {
                                                Log.d("push", "failure");
                                            }
                                        }
                                    }
                                    );

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
            }

        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    }



