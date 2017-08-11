package com.example.monamagdy.tayar;

import android.app.Activity;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.SaveCallback;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by monamagdy on 2/28/16.
 */
public class tayar_view extends Activity {
   long time;
     String time_from_server="";
    SharedPreferences.Editor editor;
    String MY_PREFS_NAME="MyPrefsFile";
    ArrayAdapter<String> adapter;
    ProgressDialog dialog1;
    String listPosition;
    int pos;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tayar_view);
        ListView k = (ListView) findViewById(R.id.listView22);

        ArrayList<String> myList = (ArrayList<String>) getIntent().getStringArrayListExtra("list");
        pos=getIntent().getExtras().getInt("position");
      //  Log.d("jj", myList.toString());

        editor=getApplicationContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        k.setAdapter(adapter);
        k.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {
          String time_from_server;
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {
                        listPosition = String.valueOf(parent.getItemAtPosition(position));
                   if(pos==0) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Not");
                    int q=   listPosition.indexOf('\n');
                    String name_of_client=  listPosition.substring(18, q);
                       String branch_of_client;
                       String number_of_client;
                    Log.d("name is",name_of_client);
                       //s = s.substring(s.indexOf("(") + 1);
                     listPosition=listPosition.substring(listPosition.indexOf( "فرع :"));

                     branch_of_client=  listPosition.substring(listPosition.indexOf(':') + 1,listPosition.indexOf('\n'));
                       Log.d("branch",branch_of_client);

                       listPosition=listPosition.substring(listPosition.indexOf(" رقم: "));
                       number_of_client= listPosition.substring(listPosition.indexOf(':') + 2,listPosition.lastIndexOf('\n'));
                       Log.d("number",number_of_client);
                       query.whereEqualTo("Client_Name", name_of_client);
                 query.whereEqualTo("Branch_Name", branch_of_client);
                       query.whereEqualTo("Client_Number", number_of_client);
                query.getFirstInBackground(new GetCallback<ParseObject>() {

                    public void done(final ParseObject object, ParseException e) {
                        if (object == null) {
                            Log.d("take_order", "This notification is saved as null.");
                        } else {

                            final String current_tayar = ParseUser.getCurrentUser().get("username").toString();
                            String x = ParseUser.getCurrentUser().get("Tayar_State").toString();
                            String order_updated = object.get("Order_Status").toString();
                            if ((x.contains("free")) && (order_updated.contains("pending"))) {

                                AlertDialog.Builder alert_builder = new AlertDialog.Builder(tayar_view.this);
                                alert_builder.setMessage("هل متاكد من رغبتك في خدمة هذا الطلب ؟").setCancelable(false)
                                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {
                                                    public void done(String result, ParseException e) {
                                                        if (e == null) {
                                                            Log.d("Cloud Code", result);

                                                            final String c = ParseUser.getCurrentUser().getObjectId().toString();
                                                            final String current_tayar = ParseUser.getCurrentUser().get("username").toString();
                                                            object.put("Order_Status", "Serving");
                                                            object.put("Handled_by", current_tayar);
                                                            object.put("Alert_50min","false");
                                                            ParseUser.getCurrentUser().put("Tayar_State", "serving");
                                                            object.put("Time_Take_was_pressed", String.valueOf(result));
                                                            Log.d("time saved in db class", String.valueOf(result));
                                                            ParseUser.getCurrentUser().saveInBackground();
                                                            ParseACL acl = new ParseACL();
                                                            acl.setPublicReadAccess(true);
                                                            acl.setPublicWriteAccess(true);
                                                            object.setACL(acl);    /*updated here*/
                                                            editor.putString(c, result);
                                                            editor.commit();
                                                            object.saveInBackground(new SaveCallback() {
                                                                public void done(ParseException e) {
                                                                    if (e != null) {
                                                                        e.printStackTrace();
                                                                        Log.d("take_order", "Notification Status is successfully updated.");
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
                            } else if (x.contains("serving")) {
                                AlertDialog.Builder builder_not = new AlertDialog.Builder(tayar_view.this);
                                builder_not.setMessage("طلبك الحالي لم يكتمل بعد");
                                AlertDialog dialog = builder_not.create();
                                dialog.show();
                            } else if (order_updated.contains("Serving"))//order is in the list but you cant take it
                            {
                                AlertDialog.Builder builder_not = new AlertDialog.Builder(tayar_view.this);
                                builder_not.setMessage("هذا الطلب في الخدمة");
                                AlertDialog dialog = builder_not.create();
                                dialog.show();
                            }
                        }
                    }
                });
            }
                        if(pos==1) {
                           /*
                            dialog1 = new ProgressDialog(tayar_view.this); // this = YourActivity
                            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            dialog1.setMessage("تحميل...");
                            dialog1.setIndeterminate(false);
                            dialog1.setCanceledOnTouchOutside(false);
                            dialog1.setCancelable(true);
                            dialog1.show();
                            */


                 //           String x = listPosition.substring(0, listPosition.indexOf('.'));
               //       final  int q=     x.lastIndexOf('\n');
                          //  query.whereEqualTo("Client_Number", listPosition.substring(0, q));
                            int q=   listPosition.indexOf('\n');
                            final String name_of_client=  listPosition.substring(18, q);
                            final String branch_of_client;
                            final String number_of_client;
                            Log.d("name is",name_of_client);
                            //s = s.substring(s.indexOf("(") + 1);
                            listPosition=listPosition.substring(listPosition.indexOf( "فرع :"));

                            branch_of_client=  listPosition.substring(listPosition.indexOf(':') + 1,listPosition.indexOf('\n'));
                            Log.d("branch",branch_of_client);

                            listPosition=listPosition.substring(listPosition.indexOf(" رقم: "));
                            number_of_client= listPosition.substring(listPosition.indexOf(':') + 2,listPosition.indexOf('\n'));
                            Log.d("number",number_of_client);
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Not");
                            query.whereEqualTo("Client_Name", name_of_client);
                            query.whereEqualTo("Branch_Name", branch_of_client);
                            query.whereEqualTo("Client_Number", number_of_client);

                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                   public void done(final ParseObject object, ParseException e) {
                                     if (object == null) {
                                         Log.d("hey", "This notification is saved as null.");
                                                }
                                                else
                                                {
                                     final String current_tayar = ParseUser.getCurrentUser().get("username").toString();
                                           if (object.get("Handled_by") != null) {
                                               Log.d("hey", "Handled_by.");
                                                if (object.get("Handled_by").toString().contains(current_tayar)) {
                                                    Intent intent = new Intent(tayar_view.this, done.class);
                                                    intent.putExtra("name",name_of_client);
                                                    intent.putExtra("branch",branch_of_client);
                                                    intent.putExtra("phone",number_of_client);
                                                    //intent.putExtra("notification", listPosition.substring(0,q));
                                                    startActivity(intent);
                                                   // dialog1.dismiss();
                                                }
                                                else {
                                                    AlertDialog.Builder builder_not = new AlertDialog.Builder(tayar_view.this);
                                                    builder_not.setMessage("Sorry, this order is not yours!");
                                                    AlertDialog dialog = builder_not.create();
                                                    dialog.show();
                                                }
                                            }

                                    }

                                }

                }
    );
}
                    }
    });


    }


    }
