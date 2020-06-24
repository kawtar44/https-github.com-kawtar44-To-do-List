package com.example.to_do_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private ArrayList<String> todoItems;
    private EditText mavariableEditText;
    private ListView mavariableListView;
    //private ArrayAdapter<String> aa;
    private Button mavariableButton;
    private NotesDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        mavariableEditText=findViewById(R.id.editText);
        mavariableListView=findViewById(R.id.list);
        mavariableButton=findViewById(R.id.button);
        //todoItems = new ArrayList<String>();
        //aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,todoItems);
        //mavariableListView.setAdapter(aa);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();

        mavariableEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()== KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                {
                    mDbHelper.createNote(mavariableEditText.getText().toString(), "");
                    fillData();
                    //todoItems.add(mavariableEditText.getText().toString());
                    //aa.notifyDataSetChanged();
                    mavariableEditText.setText("");
                    return true;
                }
                return false;
            }
        });

        mavariableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todoItems.add(mavariableEditText.getText().toString());
                //aa.notifyDataSetChanged();
                mDbHelper.createNote(mavariableEditText.getText().toString(), "");
                fillData();
                mavariableEditText.setText("");


            }
        });
        mavariableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todoItems.remove(position);
                //aa.notifyDataSetChanged();
                mDbHelper.deleteNote(id);
                fillData();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.supprimer_tout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.alert_supprimer_message)
                    .setTitle(R.string.alert_supprimer_title)
                    .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //todoItems.clear();
                            //aa.notifyDataSetChanged();
                            mDbHelper.deleteAll();
                            fillData();


                        }
                    })
                    .setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();



            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to,0);
        mavariableListView.setAdapter(notes);

    }
}
