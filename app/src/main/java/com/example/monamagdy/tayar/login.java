package com.example.monamagdy.tayar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import android.app.AlertDialog;
import com.parse.ParseUser;
public class login extends Activity{

     String pass;
    ProgressDialog dialog1;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText user_name;
        user_name = (EditText) findViewById(R.id.editText);
      final CheckBox  remember_me = (CheckBox)findViewById(R.id.checkBox);

        final EditText password;
        password = (EditText) findViewById(R.id.editText2);
        Button Submit = (Button) findViewById(R.id.button3);
        final String MY_PREFS_NAME="MyPrefsFile";
        final SharedPreferences.Editor editor=getBaseContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

        final   AlertDialog.Builder builder1 = new AlertDialog.Builder(login.this);

        Submit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                dialog1 = new ProgressDialog(login.this); // this = YourActivity
                dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog1.setMessage("Authenticating user..");
                dialog1.setIndeterminate(false);
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.setCancelable(true);
                dialog1.show();
                pass = password.getText().toString();
                username = user_name.getText().toString();

                if (remember_me.isChecked())
                {
                    editor.putString("username",username);
                    editor.putString("password", pass);
                    editor.putBoolean("remember",true);
                    editor.commit();
                }
                    ParseUser.logInInBackground(username, pass, new

                                    LogInCallback() {
                                        public void done(ParseUser user, ParseException e) {
                                            if (user != null) {
                                                String ok;
                                                  ok = ParseUser.getCurrentUser().get("Value").toString();
                                                if (ok.matches("ADMIN")) {
                                                    startActivity(new Intent(login.this, entry.class));
                                                    Log.d("USER OR ADMIN OR TAYAR", "ADMIN");
                                                } else if (ok.matches("USER")) {

                                                    startActivity(new Intent(login.this, user_landing_page.class));

                                                    Log.d("USER OR ADMIN OR TAYAR", "USER");
                                                } else if (ok.matches("TAYAR")) {

                                                    startActivity(new Intent(login.this, Tayar_Landing_page.class));
                                                    Log.d("USER OR ADMIN OR TAYAR", "TAYAR");
                                                }

                                                // Hooray! The user is logged in.
                                                Log.d("login", "success");
                                            } else {
                                                Log.d("login", "failed");
                                                builder1.setMessage("Please enter the correct username and password");
                                                AlertDialog alert11 = builder1.create();
                                                alert11.show();
                                                startActivity(new Intent(login.this, login.class));
                                            }
                                        }
                                    }
                    );

            }

        });


    }

    @Override
    public void onBackPressed() {

Intent  intent= new Intent((login.this),MainActivity.class);
        startActivity(intent);
/*
        Intent intent;
        if(Tayar_Landing_page.sign) { //tayar signed out
            intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        }
        if(entry.sign) { //admin signed out
            intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        }
        if(user_landing_page.sign) { //user signed out
            intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        }
*/

    }

}


