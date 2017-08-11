package com.example.monamagdy.tayar;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import com.parse.ParsePush;
import android.provider.Settings.Secure;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

public class starter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Register any ParseObject subclass. Must be done before calling Parse.initialize()
        //  ParseObject.registerSubclass(database_parse.class);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("8a9obA98R1LT83MgJPqfreeTpxR1BoTLRFoRjVks")
                .server("http://tayar.herokuapp.com/parse")   // '/' important after 'parse'
                .build());

        // Add your initialization code here
      //  Parse.initialize(this, "8a9obA98R1LT83MgJPqfreeTpxR1BoTLRFoRjVks", "Q8zQDWtCGtUgP9Jkl5hu6oIhMzl3ZIUB8NmIqxA2");
        ParseInstallation.getCurrentInstallation().saveInBackground();

         //   PushService.setDefaultPushCallback(this, MainActivity.class);


         String android_id = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);

        Log.e("LOG","android id >>" + android_id);

       // PushService.setDefaultPushCallback(this, MainActivity.class);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("Device_ID",android_id);

        installation.saveInBackground();




        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        Intent i = new Intent(this, splash.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  startActivity(new Intent(starter.this, spalsh_activity.class));


        //  ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
