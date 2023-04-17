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

import com.example.dreamjournal.fileio.CSVDreamDataAccess;
import com.example.dreamjournal.models.Dream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DreamListActivity extends AppCompatActivity {

    public static final String TAG = "DreamListActivity";

    private ListView lsDreams;
    private Dreamable da;
    private ArrayList<Dream> allDreams;
    private Button btnAddDream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_list);

        btnAddDream = findViewById(R.id.btnAddDream);
        btnAddDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DreamListActivity.this, DreamDetailsActivity.class);
                startActivity(i);
            }
        });


        lsDreams = findViewById(R.id.lsDreams);
        da = new CSVDreamDataAccess(this);
        allDreams = da.getAllDreams();

        // if there are no dreams - navigate to details activity
        if(allDreams == null || allDreams.size() == 0){
            Intent i = new Intent(this, DreamDetailsActivity.class);
            startActivity(i);
        }

        setDreamAdapter();
    }

    public void setDreamAdapter(){
        ArrayAdapter<Dream> adapter = new ArrayAdapter(this, R.layout.custom_dream_list_item, R.id.lblDescription, allDreams){
            @Override
            public View getView(int position, View convertView, ViewGroup parentListView){

                // call the super method and get the view that it returns (an instance of our custom view)
                View listItemView = super.getView(position, convertView, parentListView);
                // get handles on the UI stuff that we want to control that are in the custom view)
                TextView lblCategory = listItemView.findViewById(R.id.lblCategory);
                TextView lblDescription = listItemView.findViewById(R.id.lblDescription);
                TextView lblDueDate = listItemView.findViewById(R.id.lblDueDate);
                // get the Dream that is being displayed in the view
                Dream currentDream = (Dream) allDreams.get(position);
                // bind the Dream data to the view
                SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
                String dateStr = dateFormat.format(currentDream.getDate());
                lblCategory.setText(currentDream.getCategory());
                lblDescription.setText(currentDream.getDescription());
                lblDueDate.setText(dateStr);

                // listen for clicks on the row/view
                listItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DreamListActivity.this, DreamDetailsActivity.class);
                        i.putExtra(DreamDetailsActivity.EXTRA_DREAM_ID, currentDream.getId());
                        startActivity(i);
                    }
                });

                return listItemView;
            }
        };
        lsDreams.setAdapter(adapter);
    }

}