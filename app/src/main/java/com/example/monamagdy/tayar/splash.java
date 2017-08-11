package com.example.monamagdy.tayar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Handler;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
    public class splash extends Activity {

        /** Duration of wait **/
        private final int SPLASH_DISPLAY_LENGTH = 1000;
        SharedPreferences prefs;
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_splash);
            Log.d("ss", "ff");
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            final String MY_PREFS_NAME="MyPrefsFile";
            prefs =  getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);



            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                     Log.d("already logged in","yes");
                        String pass_user = prefs.getString("username", null);
                        String pass_login= prefs.getString("password",null);

                        ParseUser.logInInBackground("t", "t", new

                                        LogInCallback() {
                                            public void done(ParseUser user, ParseException e) {
                                                if (user != null) {
                                                    String ok;

                                                    ok = ParseUser.getCurrentUser().get("Value").toString();
                                                    if (ok.matches("ADMIN")) {
                                                        startActivity(new Intent(splash.this, entry.class));
                                                        // user_name.setText(username);
                                                        //  password.setText(pass);
                                                        Log.d("USER OR ADMIN OR TAYAR", "ADMIN");
                                                    } else if (ok.matches("USER")) {

                                                        startActivity(new Intent(splash.this, user_landing_page.class));

                                                        Log.d("USER OR ADMIN OR TAYAR", "USER");
                                                    } else if (ok.matches("TAYAR")) {

                                                        startActivity(new Intent(splash.this, Tayar_Landing_page.class));
                                                        Log.d("USER OR ADMIN OR TAYAR", "TAYAR");
                                                    }

                                                    // Hooray! The user is logged in.
                                                    Log.d("login", "success");
                                                } else {

                                                    startActivity(new Intent(splash.this, login.class));
                                                }
                                            }
                                        }
                        );

                    } else {
                        Log.d("already logged in","no");
                        Intent mainIntent = new Intent(splash.this,MainActivity.class);
                        splash.this.startActivity(mainIntent);
                        splash.this.finish();
                    }

                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }


