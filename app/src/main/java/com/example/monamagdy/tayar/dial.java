package com.example.monamagdy.tayar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.net.Uri;

import com.parse.ParseUser;

/**
 * Created by monamagdy on 3/26/16.
 */
public class dial extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial);
        String s,em;
        int start,end;
     //   TextView text=(TextView)findViewById(R.id.textView9);
        TextView call=(TextView)findViewById(R.id.textView11);
        String display_from_tayr=getIntent().getExtras().getString("pn");
        call.setText(display_from_tayr);

           if (!display_from_tayr.equals("")) {
               Uri number = Uri.parse("tel:" + display_from_tayr);
               Intent dial = new Intent(Intent.ACTION_DIAL, number);
               startActivity(dial);
           }

       /*
        String  display_from_admin=getIntent().getExtras().getString("pn");
        text.setText(display_from_admin);

        Log.d("em", display_from_admin);
        call.setText(display_from_admin);
        if (!display_from_admin.equals("")) {
            Uri number = Uri.parse("tel:" + display_from_admin);
            Intent dial = new Intent(Intent.ACTION_DIAL, number);
            startActivity(dial);

        }
        */


    }
}