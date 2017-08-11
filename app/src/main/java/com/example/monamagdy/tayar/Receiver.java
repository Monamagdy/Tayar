package com.example.monamagdy.tayar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.Date;
public class Receiver extends ParsePushBroadcastReceiver
{
    ArrayList<String> notif_list;
    String title="";
    Random random = new Random();
    Intent intent;
    ArrayList<String> notifications ;
    @Override
    protected void onPushReceive(final Context context, Intent intent)
    {
        Log.e("Push", "Received");
        notifications = new ArrayList<String>();

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            if (json.has("alert")) {
                title = json.getString("alert");
                Log.d("Notification", title);

            }

            String user=ParseUser.getCurrentUser().get("Value").toString();

            if(user.contains("TAYAR")||(user.contains("ADMIN")))
              generateNotification(context);

        }
        catch(JSONException e) {}
    }

    private void generateNotification(final Context context) {

        Date today,tomorrow;

            notifications.add(title);
        intent = new Intent(context, display_notification.class);
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Egypt"));
        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Calendar rightNow = Calendar.getInstance();

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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notifications").orderByDescending("createdAt");
        query.whereGreaterThanOrEqualTo("createdAt", today); //6am
        query.whereLessThan("createdAt", tomorrow); //12pm
        notif_list=   new ArrayList<String>();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    String orders = null;
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject ob = objects.get(i);
                        orders = ob.get("notifications").toString();
                        // Log.d("orders",orders);
                        if (!(notif_list.contains(orders)))
                            notif_list.add(orders);
                    }

                }


                intent.putStringArrayListExtra("you", notif_list);
                for (int i = 0; i < notif_list.size(); i++) {
                   // Log.d("list", notif_list.get(i).toString());
                }

                            Log.d("tayar", "entry_notification");
                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                 Log.d("list_size", String.valueOf(notif_list.size()));
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.image1).setContentTitle("Tayar")
                                    .setContentText(title).setOngoing(false).setAutoCancel(true);

                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                            builder.setContentIntent(contentIntent);
                            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                            builder.setLights(Color.RED, 3000, 3000);
                            builder.setSound(alarmSound);
                              NotificationManager manager;
                            // Add as notification
                            manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                            int m = random.nextInt(9999 - 1000) + 1000;
                            manager.notify(m, builder.build());

            }
        });

    }
                    }