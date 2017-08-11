package com.example.monamagdy.tayar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class display_notification extends Activity {

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_notification);
        final  ListView k = (ListView) findViewById(R.id.listView4);
       // final ArrayList<String> myList = new ArrayList<String> ();
         ArrayList<String> myList    = getIntent().getStringArrayListExtra("you");
//        myList.add("kk");
        Log.d("jj", myList.toString());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        k.setAdapter(adapter);
    }


}