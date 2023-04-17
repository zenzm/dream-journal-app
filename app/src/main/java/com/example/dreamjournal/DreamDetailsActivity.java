package com.example.dreamjournal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dreamjournal.fileio.CSVDreamDataAccess;
import com.example.dreamjournal.models.Dream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DreamDetailsActivity extends AppCompatActivity {

    public static final String TAG = "DreamDetailsActivity";
    public static final String EXTRA_DREAM_ID = "dreamId";

    Dreamable da;
    Dream dream;

    Spinner spinner;
    EditText txtDescription;
    EditText txtDate;
    Button btnSave;
    Button btnDelete;
    Button btnShowDatePicker;

    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_details);

        spinner = findViewById(R.id.spinner);
        txtDescription = findViewById(R.id.txtDescription);
        txtDate = findViewById(R.id.txtDate);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnShowDatePicker = findViewById(R.id.btnShowDatePicker);

        btnShowDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(save()){
                    Intent i = new Intent(DreamDetailsActivity.this, DreamListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(DreamDetailsActivity.this, "Unable to save dream", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //Log.d(TAG, "Delete task: " + task.getId());
//                da.deleteTask(task);
//                Intent i = new Intent(TaskDetailsActivity.this, TaskListActivity.class);
//                startActivity(i);
                showDeleteDialog();
            }
        });

        da = new CSVDreamDataAccess(this);
        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_DREAM_ID, 0);
        if(id > 0){
            dream = (Dream) da.getDreamById(id);
            //Log.d(TAG, dream.toString());
            putDataIntoUI();
            btnDelete.setVisibility(View.VISIBLE);
        }

    }


    private void putDataIntoUI(){
        if(dream != null){
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    spinner.setSelection(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            txtDescription.setText(dream.getDescription());
            txtDate.setText(sdf.format(dream.getDate()));

        }
    }

    private boolean validate(){
        boolean isValid = true;

        if(txtDescription.getText().toString().isEmpty()){
            isValid = false;
            txtDescription.setError("You must enter a description");
        }

        Date date = null;
        if(txtDate.getText().toString().isEmpty()){
            isValid = false;
            txtDate.setError("You must enter a date");
        } else{
            try {
                date = sdf.parse(txtDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                isValid = false;
                txtDate.setError("Enter a valid date");
            }

        }
        return isValid;
    }

    private boolean save(){
        if(validate()){
            getDataFromUI();
            if (dream.getId() > 0){
                Log.d(TAG, "UPDATE DREAM");
                try {
                    da.updateDream(dream);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    return false;
                }
            } else {
                Log.d(TAG, "INSERT DREAM");
                try {
                    da.insertDream(dream);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void getDataFromUI(){
        String category = spinner.getSelectedItem().toString();
        String desc = txtDescription.getText().toString();
        String dueDateStr = txtDate.getText().toString();

        Date date = null;
        try {
            date = sdf.parse(dueDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Unable to parse date from string");
        }

        if(dream != null){
            dream.setCategory(category);
            dream.setDescription(desc);
            dream.setDate(date);
        } else {
            dream = new Dream(category, desc, date);
        }

    }

    private void showDeleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Dream");
        alert.setMessage("Are you sure you want to delete this dream?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                da.deleteDream(dream);
                //dialogInterface.dismiss();
                startActivity(new Intent(DreamDetailsActivity.this, DreamListActivity.class));

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    private void showDatePicker() {
        Date today = new Date();
        int year = today.getYear() + 1900;
        int month = today.getMonth();
        int day = today.getDate();

        DatePickerDialog dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                String selectedDate = (m + 1) + "/" + d + "/" + y;
                txtDate.setText(selectedDate);
            }
        }, year, month, day);
        dp.show();
    }


}