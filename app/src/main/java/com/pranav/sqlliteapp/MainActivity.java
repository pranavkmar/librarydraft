package com.pranav.sqlliteapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase checkbook = null;

    Button retrieve = null;

    Button save = null;
    Button clear = null;
    Button delete = null;

    EditText number = null;
    TextView saveTitle = null;
    EditText checkdate = null;
    EditText payee = null;
    EditText chequenosave = null;
    EditText amount = null;
    EditText notes = null;

    Context myContext = null;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myContext = this.getApplicationContext();

        retrieve = (Button) findViewById(R.id.retrievebutton);

        save = (Button) findViewById(R.id.savebutton);


        delete = (Button) findViewById(R.id.deletebutton);

        number = (EditText) findViewById(R.id.checkno);
        chequenosave = (EditText) findViewById(R.id.chequenoSave);
        checkdate = (EditText) findViewById(R.id.date);

        payee = (EditText) findViewById(R.id.payee);
        saveTitle = (TextView) findViewById(R.id.saveTitle);
        amount = (EditText) findViewById(R.id.amount);

        notes = (EditText) findViewById(R.id.notes);
        // Create Database if necessary or open it if exists

        checkbook = openOrCreateDatabase("checkbook.db", MODE_PRIVATE, null); // in Mode SQLiteDatabase.CREATE_IF_NECESSARY

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

            checkbook.execSQL("create table checks(cheque_number integer primary key not null, date text,  payee text, amount real,  notes text)");


        }

        Button reset = (Button) findViewById(R.id.reset);

        retrieve.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String key = number.getText().toString();

                Cursor c = checkbook.query("checks", null, "cheque_number=?",

                        new String[]{key}, null, null, null, null);

                if (c.getCount() == 0) {

                    Toast.makeText(myContext, "no record exists",

                            Toast.LENGTH_SHORT).show();

                } else {
                    saveTitle.setText("Cheque Details from DataBase");
                    c.moveToFirst();
                    chequenosave.setText(c.getString(0));
                    checkdate.setText(c.getString(1));

                    payee.setText(c.getString(2));

                    amount.setText(String.valueOf(c.getDouble(3)));

                    notes.setText(c.getString(4));

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String key = chequenosave.getText().toString();

                Cursor c = checkbook.query("checks", null, "cheque_number=?",

                        new String[]{key}, null, null, null, null);

                if (c.getCount() > 0) {

                    ContentValues cv = new ContentValues();
                    cv.put("cheque_number", key);
                    cv.put("date", checkdate.getText().toString());

                    cv.put("payee", payee.getText().toString());

                    cv.put("amount",

                            Double.parseDouble(amount.getText().toString()));

                    cv.put("notes", notes.getText().toString());

                    checkbook.update("checks", cv, "cheque_number=?", new String[]{

                            key});

                    Toast.makeText(myContext, "Record was Updated.",

                            Toast.LENGTH_SHORT);
                } else {

                    ContentValues cv = new ContentValues();

                    cv.put("cheque_number", key);

                    cv.put("date", checkdate.getText().toString());

                    cv.put("payee", payee.getText().toString());

                    cv.put("amount", Double.parseDouble(amount.getText().toString()));

                    cv.put("notes", notes.getText().toString());

                    checkbook.insert("checks", null, cv);

                    Toast.makeText(myContext, "Record was Saved.",

                            Toast.LENGTH_SHORT).show();

                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String key = chequenosave.getText().toString();

                checkbook.delete("checks", "cheque_number=?", new String[]{key
                });
                chequenosave.setText(null);
                number.setText(null);

                checkdate.setText(null);

                amount.setText(null);

                payee.setText(null);

                notes.setText(null);

                Toast.makeText(myContext, "Record was deleted.",

                        Toast.LENGTH_SHORT).show();

            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdate.setText("");
                amount.setText("");
                chequenosave.setText("");
                payee.setText("");
                number.setText("");
                notes.setText("");
                saveTitle.setText("SAVE CHEQUE DETAILS");
            }
        });

        checkdate.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == checkdate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            checkdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}


