package com.example.dreamjournal.fileio;

import android.content.Context;
import android.util.Log;

import com.example.dreamjournal.Dreamable;
import com.example.dreamjournal.models.Dream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVDreamDataAccess implements Dreamable {

    public static final String TAG = "CSVDreamDataAccess";
    public static final String DATA_FILE = "dreams.csv";

    private ArrayList<Dream> allDreams;
    private Context context;

    public CSVDreamDataAccess(Context c){
        this.context = c;
        this.allDreams = new ArrayList<>();
        loadDreams();

    }


    @Override
    public ArrayList<Dream> getAllDreams() {
        loadDreams();
        return allDreams;
    }

    @Override
    public Dream getDreamById(long id) {
        for(Dream d : allDreams){
            if(id == d.getId()){
                return d;
            }
        }
        return null;
    }

    @Override
    public Dream insertDream(Dream d) throws Exception {
        if(d.isValid()){
            long newId = getMaxId() + 1;
            d.setId(newId);
            allDreams.add(d);
            saveDreams();
        }else{
            throw new Exception("Failed to insert Dream: Invalid Dream");
        }
        return d;
    }

    @Override
    public Dream updateDream(Dream d) throws Exception {
        if(d.isValid()){
            Dream dreamToUpdate = getDreamById(d.getId());
            dreamToUpdate.setCategory(d.getCategory());
            dreamToUpdate.setDescription(d.getDescription());
            dreamToUpdate.setDate(d.getDate());
            saveDreams();
        }else{
            throw new Exception("Failed to update Task: Invalid Task");
        }
        return d;
    }

    @Override
    public int deleteDream(Dream d) {
        Dream dreamToRemove = getDreamById(d.getId());
        if(dreamToRemove != null){
            allDreams.remove(allDreams.indexOf(dreamToRemove));
            saveDreams();
            return 1;
        }else{
            return 0;
        }
    }

    private String convertDreamToCSV(Dream d){

        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        String dateStr = null;

        try {
            dateStr = dateFormat.format(d.getDate());
        }catch (Exception e){
            dateStr = "UNABLE TO CONVERT DATE TO A STRING!";
        }

        return String.format("%d,%s,%s,%s", d.getId(), d.getCategory(), d.getDescription(), dateStr);
    }

    private Dream convertCSVToDream(String csvLine) {

        Log.d(TAG, "CSV Line: " + csvLine);

        String[] csvValues = csvLine.trim().split(",");

        if(csvValues.length == 4){
            long id = Long.parseLong(csvValues[0]);
            String category = csvValues[1];
            String desc = csvValues[2];
            String dateStr = csvValues[3];

            SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
            Date date = null;
            try{
                date = dateFormat.parse(dateStr);
            }catch (Exception e) {
                Log.d(TAG, "UNABLE TO PARSE DATE STRING");
            }

            //create a new Dream object with the data
            Dream d = new Dream(id, category, desc, date);
            return d;
        }else{
            Log.d(TAG, "THERE SHOULD BE 4 ELEMENTS IN THE CELLS ARRAY");
        }

        return null;
    }

    private void loadDreams(){
        ArrayList<Dream> dataList = new ArrayList<>();
        String dataString = FileHelper.readFromFile(DATA_FILE, context);
        if(dataString == null){
            Log.d(TAG, "No data file");
            return;
        }

        String[] lines = dataString.trim().split("\n");

        for(String line : lines){
            Dream d = convertCSVToDream(line);
            if(d != null){
                dataList.add(d);
            }
        }
        this.allDreams = dataList;
    }

    private void saveDreams(){
        String dataString = "";
        StringBuilder sb = new StringBuilder();
        for(Dream d : allDreams){
            String csv = convertDreamToCSV(d) + "\n";
            sb.append(csv);
        }

        dataString = sb.toString();
        if(FileHelper.writeToFile(DATA_FILE, dataString, context)){
            Log.d(TAG, "Successfully saved data to file");
        }else{
            Log.d(TAG, "Failed to save data to file");
        }

    }

    private long getMaxId(){
        long maxId = 0;
        for(Dream d : allDreams){
            long dId = d.getId();
            maxId = dId > maxId ? dId : maxId;
        }
        return maxId;
    }
}
