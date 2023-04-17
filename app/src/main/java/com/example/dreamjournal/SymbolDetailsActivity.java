package com.example.dreamjournal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dreamjournal.fileio.CSVSymbolDataAccess;
import com.example.dreamjournal.models.Symbol;


public class SymbolDetailsActivity extends AppCompatActivity {

    public static final String TAG = "SymbolDetailsActivity";
    public static final String EXTRA_SYMBOL_ID = "symbolId";

    Symbolable da;
    Symbol symbol;

    EditText txtName;
    EditText txtDescription;
    Button btnSave;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbol_details);

        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(save()){
                    Intent i = new Intent(SymbolDetailsActivity.this, SymbolListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(SymbolDetailsActivity.this, "Unable to save Symbol", Toast.LENGTH_SHORT).show();
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

        da = new CSVSymbolDataAccess(this);
        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_SYMBOL_ID, 0);
        if(id > 0){
            symbol = da.getSymbolById(id);
            Log.d(TAG, symbol.toString());
            putDataIntoUI();
            btnDelete.setVisibility(View.VISIBLE);
        }

    }

    private void putDataIntoUI(){
        if(symbol != null){

            txtName.setText(symbol.getName());
            txtDescription.setText(symbol.getDescription());
        }
    }

    private boolean validate(){
        boolean isValid = true;

        if(txtName.getText().toString().isEmpty()){
            isValid = false;
            txtName.setError("You must enter a name");
        }

        if(txtDescription.getText().toString().isEmpty()){
            isValid = false;
            txtDescription.setError("You must enter a description");
        }

        return isValid;
    }

    private boolean save(){
        if(validate()){
            getDataFromUI();
            if (symbol.getId() > 0){
                Log.d(TAG, "UPDATE SYMBOL");
                try {
                    da.updateSymbol(symbol);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    return false;
                }
            } else {
                Log.d(TAG, "INSERT SYMBOL");
                try {
                    da.insertSymbol(symbol);
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
        String name = txtName.getText().toString();
        String desc = txtDescription.getText().toString();

        if(symbol != null){
            symbol.setName(name);
            symbol.setDescription(desc);
        } else {
            symbol = new Symbol(name, desc);
        }

    }

    private void showDeleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Symbol");
        alert.setMessage("Are you sure you want to delete this symbol?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                da.deleteSymbol(symbol);
                startActivity(new Intent(SymbolDetailsActivity.this, SymbolListActivity.class));

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

}