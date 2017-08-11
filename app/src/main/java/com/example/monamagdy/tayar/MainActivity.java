package com.example.monamagdy.tayar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
public class MainActivity extends Activity {
    SharedPreferences prefs;
    AlertDialog.Builder builder1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        final String MY_PREFS_NAME="MyPrefsFile";

        Button button= (Button) findViewById(R.id.button5);
         prefs =  getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
         builder1 = new AlertDialog.Builder(MainActivity.this);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    first_time_check();

            }
        });


        Button new_user= (Button) findViewById(R.id.button);
        //signup
        new_user.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, new_user.class));
            }

        });
    }
    @Override
    public void onBackPressed() {

        Intent intent;

            intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);


    }
    private boolean first_time_check() {

        String first = prefs.getString("username", null);
        if((first == null)){
            Log.d("login","first");
            Intent i = new Intent(MainActivity.this, login.class);
            startActivity(i);
        }
        else
        {
            Log.d("login","not first");
            String pass_user = prefs.getString("username", null);
            String pass_login= prefs.getString("password",null);

            ParseUser.logInInBackground(pass_user, pass_login, new

                            LogInCallback() {
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null) {
                                        String ok;

                                        ok = ParseUser.getCurrentUser().get("Value").toString();
                                        if (ok.matches("ADMIN")) {
                                            startActivity(new Intent(MainActivity.this, entry.class));
                                            Log.d("USER OR ADMIN OR TAYAR", "ADMIN");
                                        } else if (ok.matches("USER")) {

                                            startActivity(new Intent(MainActivity.this, user_landing_page.class));

                                            Log.d("USER OR ADMIN OR TAYAR", "USER");
                                        } else if (ok.matches("TAYAR")) {

                                            startActivity(new Intent(MainActivity.this, Tayar_Landing_page.class));
                                            Log.d("USER OR ADMIN OR TAYAR", "TAYAR");
                                        }

                                        // Hooray! The user is logged in.
                                        Log.d("login", "success");
                                    } else {
                                        Log.d("login", "failed");
                                        builder1.setMessage("Please enter the correct username and password");
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                        startActivity(new Intent(MainActivity.this, login.class));
                                    }
                                }
                            }
            );

        }
        return false;
    }

}
