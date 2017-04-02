package com.example.tom.todolist2;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import android.speech.tts.TextToSpeech.OnInitListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
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
    private TextToSpeech speaker;
    private String tag = "Widgets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speaker.setLanguage(Locale.US);
                }
            }
        });

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
        File list = new File("list.txt");
        if(list.exists()) {
            try {
                InputStream in = openFileInput(file);
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr);
                String str = null;

                while ((str = reader.readLine()) != null) {
                    arrayList.add(reader.readLine());
                    arrayAdapter.notifyDataSetChanged();
                }
                reader.close();
            } catch (IOException e) {}

        }
    }


    public void speak(String output){
        speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "id 0");

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
            // Saves list, leaves app open
            case R.id.SaveList:
               try {
                   out = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE));
                   for (int i = 0; i < arrayList.size(); i++){
                       line = arrayList.get(i);
                       out.write(line + "\n");
                   }
                   out.close();
               }
               catch (IOException e) {
                   Log.e("IOTest", e.getMessage());
               }
                return true;
            //Saves list, closes app
            case R.id.CloseApp:
                try {
                    out = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE));
                    for (int i = 0; i < arrayList.size(); i++){
                        line = arrayList.get(i);
                        out.write(line + "\n");
                    }
                    out.close();
                }
                catch (IOException e) {
                    Log.e("IOTest", e.getMessage());
                }
                finish();
                return true;

            //Adds new entry at the bottom of the list, numbers entry.
            case R.id.addEntry:
                String newEntry = dataEntry.getText().toString();
                String entryNumber = arrayList.size() + 1 + ". " + newEntry;
                arrayList.add(entryNumber);
                arrayAdapter.notifyDataSetChanged();
                dataEntry.setText("");
                String added = newEntry + " was added";
                speak(added);
                return true;

            //Removes item from arrayList, updates arrayAdapter
            case R.id.deleteEntry:
                String deleteEntry = dataEntry.getText().toString();
                arrayList.remove(deleteEntry);
                arrayAdapter.notifyDataSetChanged();
                dataEntry.setText("");
                String deleted = deleteEntry + " was deleted";
                speak(deleted);
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
