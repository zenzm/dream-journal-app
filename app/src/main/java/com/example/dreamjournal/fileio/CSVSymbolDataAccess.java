package com.example.dreamjournal.fileio;

import android.content.Context;
import android.util.Log;

import com.example.dreamjournal.Symbolable;
import com.example.dreamjournal.models.Symbol;

import java.util.ArrayList;

public class CSVSymbolDataAccess implements Symbolable {

    public static final String TAG = "CSVSymbolDataAccess";
    public static final String DATA_FILE = "symbols.csv";

    private ArrayList<Symbol> allSymbols;
    private Context context;

    public CSVSymbolDataAccess(Context c){
        this.context = c;
        this.allSymbols = new ArrayList<>();
        loadSymbols();

    }


    @Override
    public ArrayList<Symbol> getAllSymbols() {
        loadSymbols();
        return allSymbols;
    }

    @Override
    public Symbol getSymbolById(long id) {
        for(Symbol s : allSymbols){
            if(id == s.getId()){
                return s;
            }
        }
        return null;
    }

    @Override
    public Symbol insertSymbol(Symbol s) throws Exception {
        if(s.isValid()){
            long newId = getMaxId() + 1;
            s.setId(newId);
            allSymbols.add(s);
            saveSymbols();
        }else{
            throw new Exception("Failed to insert Symbol: Invalid Symbol");
        }
        return s;
    }

    @Override
    public Symbol updateSymbol(Symbol s) throws Exception {
        Symbol symbolToUpdate = getSymbolById(s.getId());
        if(s.isValid()){
            symbolToUpdate.setName(s.getName());
            symbolToUpdate.setDescription(s.getDescription());
            saveSymbols();
        }else{
            throw new Exception("Failed to update Symbol: Invalid Symbol");
        }
        return s;
    }

    @Override
    public int deleteSymbol(Symbol s) {
        Symbol symbolToRemove = getSymbolById(s.getId());
        if(symbolToRemove != null){
            allSymbols.remove(allSymbols.indexOf(symbolToRemove));
            saveSymbols();
            return 1;
        }else{
            return 0;
        }
    }

    private String convertSymbolToCSV(Symbol s){
        return String.format("%d,%s,%s", s.getId(), s.getName(), s.getDescription());
    }

    private Symbol convertCSVToSymbol(String csvLine) {

        Log.d(TAG, "CSV Line: " + csvLine);

        String[] csvValues = csvLine.trim().split(",");

        if(csvValues.length == 3){
            long id = Long.parseLong(csvValues[0]);
            String name = csvValues[1];
            String desc = csvValues[2];

            //create a new Symbol object with the data
            Symbol s = new Symbol(id, name, desc);
            return s;
        }else{
            Log.d(TAG, "THERE SHOULD BE 3 ELEMENTS IN THE CELLS ARRAY");
        }

        return null;
    }

    private void loadSymbols(){
        ArrayList<Symbol> dataList = new ArrayList<>();
        String dataString = FileHelper.readFromFile(DATA_FILE, context);
        if(dataString == null){
            Log.d(TAG, "No data file");
            return;
        }

        String[] lines = dataString.trim().split("\n");

        for(String line : lines){
            Symbol s = convertCSVToSymbol(line);
            if(s != null){
                dataList.add(s);
            }
        }
        this.allSymbols = dataList;
    }

    private void saveSymbols(){
        String dataString = "";
        StringBuilder sb = new StringBuilder();
        for(Symbol s : allSymbols){
            String csv = convertSymbolToCSV(s) + "\n";
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
        for(Symbol s : allSymbols){
            long sId = s.getId();
            maxId = sId > maxId ? sId : maxId;
        }
        return maxId;
    }
}
