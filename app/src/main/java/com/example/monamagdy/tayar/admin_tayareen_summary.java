package com.example.monamagdy.tayar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by monamagdy on 3/11/16.
 */
public class admin_tayareen_summary extends Activity {

    ArrayAdapter<String> adapter;
    ListView k;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_tayareen_summary);
        k = (ListView) findViewById(R.id.listView3);
        final ArrayList<String> myList = (ArrayList<String>) getIntent().getStringArrayListExtra("list");
        Log.d("jj", myList.toString());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        k.setAdapter(adapter);

    }
}