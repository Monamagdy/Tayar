package com.example.monamagdy.tayar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.content.Intent;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;;
import com.parse.FindCallback;
import com.parse.ParseUser;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
/**
 * Created by monamagdy on 2/28/16.
 */
public class Tayar_Landing_page extends Activity {
    ListView lv;
    String db_notification, db_tayar;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems0;
    ArrayList<String> listItems1;
    ArrayList<String> listItems2;
    ArrayList<String> listItems3;
    ProgressDialog dialog1;
    public static boolean sign = false;
Date today,tomorrow;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.tayar_landing_page);
        Button signout = (Button) findViewById(R.id.button6);
        lv = (ListView) findViewById(R.id.listView88);
        final String MY_PREFS_NAME="MyPrefsFile";
        final SharedPreferences.Editor editor=getBaseContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

        String[] values = new String[]{"الطلبات الجديده", "طلبات في الخدمة","طلبات اليوم","الإشعارات"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                          dialog1 = new ProgressDialog(Tayar_Landing_page.this); // this = YourActivity
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
                                              query.whereGreaterThanOrEqualTo("createdAt", today);
                                              query.whereLessThan("createdAt", tomorrow);
                                              query.whereEqualTo("Order_Status", "pending");
                                              listItems0 = new ArrayList<String>();

                                              query.findInBackground(new FindCallback<ParseObject>() {
                                                  public void done(List<ParseObject> objects, ParseException e) {
                                                      String time="";
                                                      String branch="",name="",pn="";
                                                      if (e == null) {
                                                          for (int i = 0; i < objects.size(); i++) {
                                                              ParseObject ob = objects.get(i);
                                                              if(ob.get("Client_Number")!=null)
                                                                  pn= ob.get("Client_Number").toString();

                                                              if(ob.get("Client_Name")!=null)
                                                                  name=ob.get("Client_Name").toString();
                                                              if(ob.get("Branch_Name")!=null)
                                                                  branch=ob.get("Branch_Name").toString();

                                                              db_notification =   "من فضلك اذهب الي  "+name+'\n'+ "فرع :"+branch+'\n'+" رقم: "+pn;
                                                              if(ob.getCreatedAt()!=null) {
                                                                  time = ob.getCreatedAt().toString();
                                                                  time=time.substring(0,time.lastIndexOf(":")) ;
                                                                  time=time.substring(0,time.lastIndexOf(":"));

                                                              }
                                                           //   Log.d("Notification Retrieved", db_notification);
                                                              if (!listItems0.contains(db_notification+'\n'+"Date: "+time))
                                                                  listItems0.add(db_notification+'\n'+"Date: "+time);
                                                          }
                                                      } else {

                                                      }
                                                      Intent intent = new Intent(Tayar_Landing_page.this, tayar_view.class);
                                                      intent.putExtra("position", position);
                                                      intent.putStringArrayListExtra("list", listItems0);
                                                      Log.d("New", listItems0.toString());
                                                      startActivity(intent);
                                                      dialog1.dismiss();
                                                  }
                                              });


                                          } else if (position == 1) {
                                              final String current_tayar = ParseUser.getCurrentUser().get("username").toString();
                                              ParseQuery<ParseObject> query = ParseQuery.getQuery("Not").orderByDescending("createdAt");
                                              query.whereGreaterThanOrEqualTo("createdAt", today);
                                              query.whereLessThan("createdAt", tomorrow);
                                              query.whereEqualTo("Order_Status", "Serving");
                                              query.whereEqualTo("Handled_by", current_tayar);
                                              listItems1 = new ArrayList<String>();
                                              query.findInBackground(new FindCallback<ParseObject>() {
                                                  public void done(List<ParseObject> objects, ParseException e) {
                                                      String s,time="",branch="",pn="",name="";
                                                      if (e == null) {
                                                          for (int i = 0; i < objects.size(); i++) {
                                                              ParseObject ob = objects.get(i);
                                                              if(ob.get("Client_Number")!=null)
                                                                  pn= ob.get("Client_Number").toString();

                                                              if(ob.get("Client_Name")!=null)
                                                                  name=ob.get("Client_Name").toString();
                                                              if(ob.get("Branch_Name")!=null)
                                                                  branch=ob.get("Branch_Name").toString();

                                                              s =   "من فضلك اذهب الي  "+name+'\n'+ "فرع :"+branch+'\n'+" رقم: "+pn;

                                                              if(ob.getCreatedAt()!=null) {
                                                                  time = ob.getCreatedAt().toString();
                                                                  time=time.substring(0,time.lastIndexOf(":")) ;
                                                                  time=time.substring(0,time.lastIndexOf(":"));

                                                              }
                                                              if (ob.get("Handled_by") != null)
                                                                  if (ob.get("Handled_by").toString().contains(current_tayar)) {
                                                                      db_tayar = "Handled by: " + ob.get("Handled_by").toString();
                                                                      if (!listItems1.contains((s+'\n'+"Date: "+time + "." + '\n' + db_tayar)))
                                                                          listItems1.add(s +'\n'+"Date: "+time+ "." + '\n' + db_tayar);
                                                                  }
                                                          }
                                                      } else {

                                                      }
                                                      Intent intent = new Intent(Tayar_Landing_page.this, tayar_view.class);
                                                      intent.putExtra("position", position);
                                                      intent.putStringArrayListExtra("list", listItems1);
                                                      //  Log.d("New1", listItems1.toString());
                                                      startActivity(intent);
                                                      dialog1.dismiss();
                                                  }
                                              });

                                          } else if (position == 2) {
                                              ParseQuery<ParseObject> query = ParseQuery.getQuery("Not").orderByDescending("createdAt");
                                              query.whereGreaterThanOrEqualTo("createdAt", today);
                                              query.whereLessThan("createdAt", tomorrow);
                                              query.whereEqualTo("Order_Status", "Served");

                                              query.whereEqualTo("Handled_by", ParseUser.getCurrentUser().getUsername().toString());
                                              listItems2 = new ArrayList<String>();
                                              query.findInBackground(new FindCallback<ParseObject>() {
                                                  public void done(List<ParseObject> objects, ParseException e) {
                                                      String s="",pn="",name="",branch="";
                                                      if (e == null) {
                                                          for (int i = 0; i < objects.size(); i++) {
                                                              ParseObject ob = objects.get(i);
                                                              if(ob.get("Client_Number")!=null)
                                                                  pn= ob.get("Client_Number").toString();

                                                              if(ob.get("Client_Name")!=null)
                                                                  name=ob.get("Client_Name").toString();
                                                              if(ob.get("Branch_Name")!=null)
                                                                  branch=ob.get("Branch_Name").toString();

                                                              db_notification =   "من فضلك اذهب الي  "+name+'\n'+ "فرع :"+branch+'\n'+" رقم: "+pn;

                                                              //   db_notification = ob.get("Client_Number").toString();
                                                              if(ob.getCreatedAt()!=null) {
                                                                  s = ob.getCreatedAt().toString();
                                                                  s=s.substring(0,s.lastIndexOf(":")) ;
                                                                  s=s.substring(0,s.lastIndexOf(":"));

                                                              }

                                                              //        Log.d("Notification Retrieved", db_notification);
                                                              if (!listItems2.contains(db_notification+'\n'+"Date: "+s))
                                                                  listItems2.add(db_notification+'\n'+"Date: "+s);
                                                          }
                                                      } else {

                                                      }
                                                      Intent intent = new Intent(Tayar_Landing_page.this, tayar_view.class);
                                                      intent.putExtra("position", position);
                                                      intent.putStringArrayListExtra("list", listItems2);
                                                      Log.d("New", listItems2.toString());
                                                      startActivity(intent);
                                                      dialog1.dismiss();
                                                  }
                                              });


                                          }


                                          else if (position==3)

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
                                                               orders = ob.get("notifications").toString();
                                                               // Log.d("orders",orders);
                                                               if (!(listItems3.contains(orders)))
                                                                   listItems3.add(orders);
                                                           }

                                                       }

                                                       Intent intent = new Intent(Tayar_Landing_page.this, display_notification.class);
                                                       intent.putStringArrayListExtra("you", listItems3);
                                                       for (int i = 0; i < listItems3.size(); i++) {
                                                            Log.d("list", listItems3.get(i).toString());
                                                       }
                                                       startActivity(intent);
                                                       dialog1.dismiss();
                                                   }
                                               });

                                                   }

                                      }

                                  }

        );
        signout.setOnClickListener(new View.OnClickListener()

                                   {

                                       public void onClick(View v) {
                                           dialog1 = new ProgressDialog(Tayar_Landing_page.this); // this = YourActivity
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

                                           Intent intent = new Intent(Tayar_Landing_page.this, login.class);
                                           startActivity(intent);
                                           sign = true;
                                       }

                                   }

        );
    }

    @Override
    public void onBackPressed() {}



}