package com.pranav.sqlliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase checkbook = null;

    Button retrieve = null;

    Button save = null;

    Button delete = null;

    EditText number = null;

    EditText checkdate = null;
    EditText payee = null;

    EditText amount = null;
    EditText notes = null;

    Context myContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myContext = this.getApplicationContext();

        retrieve = (Button) findViewById(R.id.retrievebutton);

        save = (Button) findViewById(R.id.savebutton);

        delete = (Button) findViewById(R.id.deletebutton);

        number = (EditText) findViewById(R.id.checkno);

        checkdate = (EditText) findViewById(R.id.date);

        payee = (EditText) findViewById(R.id.payee);

        amount = (EditText) findViewById(R.id.amount);

        notes = (EditText) findViewById(R.id.notes);
        // Create Database if necessary or open it if exists

        checkbook = openOrCreateDatabase("checkbook.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

// Check if table exists

        boolean tableok = false;

        Cursor c = checkbook.query(

                "sqlite_master", null,

                "type=? and name=?",

                new String[]{"table", "checks"},

                null, null, null);
        if (c.getCount() > 0)
            tableok = true;
        if (!tableok) {

            checkbook.execSQL(

                    "create table checks(number integer primary key not null, date text,  payee text, amount real,  notes text)");


        }
        retrieve = (Button) findViewById(R.id.retrievebutton);

        save = (Button) findViewById(R.id.savebutton);

        delete = (Button) findViewById(R.id.deletebutton);

        number = (EditText) findViewById(R.id.checkno);

        checkdate = (EditText) findViewById(R.id.date);

        payee = (EditText) findViewById(R.id.payee);

        amount = (EditText) findViewById(R.id.amount);

        notes = (EditText) findViewById(R.id.notes);

        retrieve.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String key = number.getText().toString();

                Cursor c = checkbook.query("checks", null, "number=?",

                        new String[]{key}, null, null, null, null);

                if (c.getCount() == 0) {

                    Toast.makeText(myContext, "no record exists",

                            Toast.LENGTH_SHORT).show();

                } else {

                    c.moveToFirst();

                    checkdate.setText(c.getString(1));

                    payee.setText(c.getString(2));

                    amount.setText(String.valueOf(c.getDouble(3)));

                    notes.setText(c.getString(4));

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String key = number.getText().toString();

                Cursor c = checkbook.query("checks", null, "number=?",

                        new String[]{key}, null, null, null, null);

                if (c.getCount() > 0) {

                    ContentValues cv = new ContentValues();

                    cv.put("date", checkdate.getText().toString());

                    cv.put("payee", payee.getText().toString());

                    cv.put("amount",

                            Double.parseDouble(amount.getText().toString()));

                    cv.put("notes", notes.getText().toString());

                    checkbook.update("checks", cv, "number=?", new String[]{

                            key});

                    Toast.makeText(myContext, "Record was Updated.",

                            Toast.LENGTH_SHORT);
                } else {

                    ContentValues cv = new ContentValues();

                    cv.put("number", key);

                    cv.put("date", checkdate.getText().toString());

                    cv.put("payee", payee.getText().toString());

                    cv.put("amount",

                            Double.parseDouble(amount.getText().toString()));

                    cv.put("notes", notes.getText().toString());

                    checkbook.insert("checks", null, cv);

                    Toast.makeText(myContext, "Record was Saved.",

                            Toast.LENGTH_SHORT).show();

                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String key = number.getText().toString();

                checkbook.delete("checks", "number=?", new String[]{key
                });

                number.setText(null);

                checkdate.setText(null);

                amount.setText(null);

                payee.setText(null);

                notes.setText(null);

                Toast.makeText(myContext, "Record was deleted.",

                        Toast.LENGTH_SHORT).show();

            }
        });

    }

}


