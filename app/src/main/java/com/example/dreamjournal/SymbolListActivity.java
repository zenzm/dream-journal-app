package com.example.dreamjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dreamjournal.fileio.CSVSymbolDataAccess;
import com.example.dreamjournal.models.Symbol;

import java.util.ArrayList;

public class SymbolListActivity extends AppCompatActivity {

    public static final String TAG = "SymbolListActivity";

    private ListView lsSymbols;
    private Symbolable da;
    private ArrayList<Symbol> allSymbols;
    private Button btnAddSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbol_list);

        btnAddSymbol = findViewById(R.id.btnAddSymbol);
        btnAddSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SymbolListActivity.this, SymbolDetailsActivity.class);
                startActivity(i);
            }
        });


        lsSymbols = findViewById(R.id.lsSymbols);
        da = new CSVSymbolDataAccess(this);
        allSymbols = da.getAllSymbols();

        // if there are no Symbols - navigate to details activity
        if (allSymbols == null || allSymbols.size() == 0) {
            Intent i = new Intent(this, SymbolDetailsActivity.class);
            startActivity(i);
        }

        example3();
    }

    public void example3(){
        ArrayAdapter<Symbol> adapter = new ArrayAdapter(this, R.layout.custom_symbol_list_item, R.id.lblSymbolDescription, allSymbols){
            @Override
            public View getView(int position, View convertView, ViewGroup parentListView){

                // call the super method and get the view that it returns (an instance of our custom view)
                View listItemView = super.getView(position, convertView, parentListView);
                // get handles on the UI stuff that we want to control that are in the custom view)
                TextView lblName = listItemView.findViewById(R.id.lblSymbolName);
                TextView lblDescription = listItemView.findViewById(R.id.lblSymbolDescription);
                // get the Symbol that is being displayed in the view
                Symbol currentSymbol = allSymbols.get(position);
                // bind the Symbol data to the view
                lblName.setText(currentSymbol.getName());
                lblDescription.setText(currentSymbol.getDescription());

                // listen for clicks on the row/view
                listItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(SymbolListActivity.this, SymbolDetailsActivity.class);
                        i.putExtra(SymbolDetailsActivity.EXTRA_SYMBOL_ID, currentSymbol.getId());
                        startActivity(i);
                    }
                });

                return listItemView;
            }
        };
        lsSymbols.setAdapter(adapter);
    }


}