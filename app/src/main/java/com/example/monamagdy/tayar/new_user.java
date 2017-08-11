package com.example.monamagdy.tayar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.AlertDialog;
import android.util.Log;
import android.net.NetworkInfo;
import android.widget.Toast;

public class new_user extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);


        final EditText reg_username ;
        reg_username = (EditText) findViewById(R.id.editText3);

        final EditText reg_password ;
        reg_password= (EditText) findViewById(R.id.editText4);

        Button Submit_reg=(Button) findViewById(R.id.button4);

        final EditText phonenumber;
        phonenumber= (EditText) findViewById(R.id.editText5);

        final EditText chainname;
        chainname=(EditText) findViewById(R.id.editText6);

        final EditText branch1;
        branch1=(EditText) findViewById(R.id.editText8);

        final EditText branch2;
        branch2= (EditText) findViewById( R.id.editText9);

        Submit_reg.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(new_user.this);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(new_user.this);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(new_user.this);
                AlertDialog.Builder builder4 = new AlertDialog.Builder(new_user.this);

                builder1.setMessage("Please make sure you've got all the fields filled");
                builder2.setMessage("You are now a successfully registered user");
                builder3.setMessage("This username already exists, please choose another one.");
                builder4.setMessage("Please make sure your device is connected to the internet");
                AlertDialog alert11 = builder1.create();
                final AlertDialog alert22=builder2.create();
                final AlertDialog alert33=builder3.create();
                final AlertDialog alert44= builder4.create();

                if(isNetworkAvailable()==false)
                      alert44.show();

                final String pass,username,c1,b1,b2,phone;


                pass = reg_password.getText().toString();
                username = reg_username.getText().toString();
                phone=phonenumber.getText().toString();
                c1=chainname.getText().toString();
                b1=branch1.getText().toString();
                b2=branch2.getText().toString();

                if((c1.isEmpty())||(username.isEmpty())||(b1.isEmpty())||(pass.isEmpty())||(phone.isEmpty())||(b2.isEmpty()))
                    alert11.show();


                else {
                final  int pn = new Integer(phone).intValue();
                    ParseUser testObject = new ParseUser();
                    testObject.setUsername( username);
                    testObject.setPassword(pass);
                    testObject.put("Phone_Number", pn);
                    testObject.put("Branch1",b1);
                    testObject.put("Branch2",b2);
                    testObject.put("Chain_Name",c1);
                    testObject.put("Value","USER");
                    testObject.put("Orders",0);

                    testObject.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                Log.d("Registered", "Success");
                                Toast.makeText(getApplicationContext(), "You can use Tayar RIGHT NOW! ", Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(new_user.this, login.class);
                                startActivity(intent);
                               // alert22.show();
                            } else {
                                alert33.show();
                            }
                        }
                    });


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
