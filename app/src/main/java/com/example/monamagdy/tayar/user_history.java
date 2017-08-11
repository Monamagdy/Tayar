package com.example.monamagdy.tayar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by monamagdy on 3/3/16.
 */
public class user_history  extends Activity {
    public static long time;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_history);
      final  ListView k = (ListView) findViewById(R.id.listView111);
        ArrayList<String> myList = (ArrayList<String>) getIntent().getStringArrayListExtra("list");

       final Intent intent= new Intent(user_history.this,dial.class);
        Log.d("jj", myList.toString());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        k.setAdapter(adapter);
        /*
        k.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {
                            String itemValue = (String) k.getItemAtPosition(position);
                        intent.putExtra("text",itemValue);
                        startActivity(intent);
                    }
                });
*/
}
}