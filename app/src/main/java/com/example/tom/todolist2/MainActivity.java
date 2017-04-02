package com.example.tom.todolist2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity{
    ArrayList<String> arrayList;
    private String line;
    ArrayAdapter<String> arrayAdapter;
    private EditText dataEntry;
    String[] items = {};
    private final String file = "list.txt";
    private OutputStreamWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataEntry = (EditText)findViewById(R.id.dataEntryEdit);


        ListView listview = (ListView)findViewById(R.id.list);
        arrayList = new ArrayList<>(Arrays.asList(items));
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item, R.id.txtitem, arrayList);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = arrayList.get(position);
                dataEntry.setText(text);
            }
        });
        try {
            out = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE)); // also try MODE_APPEND
        } catch (IOException e) {}
    }


    // Creates menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Carries out action behind each menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.SaveList:
               try {
                   for (int i = 0; i < arrayAdapter.getCount(); i++){
                       line = arrayList.get(i);
                       out.write(line + " \n");
                   }
               }
               catch (IOException e) {
                   Log.e("IOTest", e.getMessage());
               }
                return true;

            case R.id.CloseApp:
                finish();
                return true;

            //Adds new entry at the bottom of the list, numbers entry.
            case R.id.addEntry:
                String newEntry = dataEntry.getText().toString();
                String entryNumber = arrayList.size() + 1 + ". " + newEntry;
                arrayList.add(entryNumber);
                arrayAdapter.notifyDataSetChanged();
                dataEntry.setText("");
                return true;

            //Removes item from arrayList, updates arrayAdapter
            case R.id.deleteEntry:
                String deleteEntry = dataEntry.getText().toString();
                arrayList.remove(deleteEntry);
                arrayAdapter.notifyDataSetChanged();
                dataEntry.setText("");
                return true;

            //Removes based on position selected, adds back to position written in editText.
            case R.id.UpdateEntry:
                String position = dataEntry.getText().toString().substring(0,1);
                int posi = Integer.parseInt(position) - 1;
                String data = dataEntry.getText().toString();
                arrayList.remove(posi);
                arrayList.add(posi,data);
                arrayAdapter.notifyDataSetChanged();
                dataEntry.setText("");
                return true;

            default:
                super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
